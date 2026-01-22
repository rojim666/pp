package com.charging.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MqttConfig implements MqttCallback {
    @Value("${mqtt.broker}") private String broker;
    @Value("${mqtt.username}") private String username;
    @Value("${mqtt.password}") private String password;
    @Value("${mqtt.topic}") private String topic;
    @Value("${mqtt.client-id}") private String clientId;

    private final ObjectMapper objectMapper;
    private final com.charging.service.ChargingStationService stationService;  // Inject Service
    private MqttClient client;

    @PostConstruct
    public void connect() throws Exception {
        client = new MqttClient(broker, clientId, new MemoryPersistence());
        var options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        client.setCallback(this);
        client.connect(options);
        // 订阅配置的 Topic (支持 /device/+/status 和 /device/+/controll)
        client.subscribe(topic, 1);
        log.info("MQTT connected and subscribed to {}", topic);
    }

    /**
     * 发送指令到设备
     *
     * @param deviceId 设备ID (MAC地址)
     * @param payload  指令内容
     */
    public void sendToDevice(String deviceId, String payload) {
        try {
            String targetTopic = "/device/" + deviceId + "/controll";
            MqttMessage message = new MqttMessage(payload.getBytes(StandardCharsets.UTF_8));
            message.setQos(1);
            if (client != null && client.isConnected()) {
                client.publish(targetTopic, message);
                log.info("Sent control command to [{}]: {}", targetTopic, payload);
            } else {
                log.warn("MQTT client not connected, failed to send to {}", targetTopic);
            }
        } catch (MqttException e) {
            log.error("Failed to send MQTT message", e);
        }
    }

    @PreDestroy
    public void close() {
        try { if (client != null) client.disconnect(); } catch (Exception ignored) {}
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.warn("MQTT connection lost", cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            var payload = new String(message.getPayload(), StandardCharsets.UTF_8);
            
            // 解析 Topic 结构： /device/{deviceId}/{type}
            // 例如: /device/34:85:18:9B:64:0C/status
            String[] parts = topic.split("/");
            if (parts.length < 4) {
                log.warn("Ignored message from unknown topic format: {}", topic);
                return;
            }
            
            String deviceId = parts[2];
            String msgType = parts[3]; // status 或 controll

            log.info("MQTT Msg from Device[{}] Type[{}]: {}", deviceId, msgType, payload);

            // 只处理状态上报
            if ("status".equals(msgType)) {
                 var node = objectMapper.readTree(payload);
                 // ... 处理原有 status 逻辑 ...
                 parseStatusPayload(deviceId, node, payload);
            } else if ("controll".equals(msgType)) {
                // 如果设备也会向 controll topic 发送消息（例如回复），在这里处理
                log.info("Received control reply from device {}", deviceId);
            }

        } catch (Exception e) {
            log.error("Failed to parse MQTT message from " + topic, e);
        }
    }

    private void parseStatusPayload(String deviceId, com.fasterxml.jackson.databind.JsonNode node, String rawPayload) {
        // Check message type field inside JSON if exists
        if (node.has("type") && !"status".equals(node.path("type").asText())) {
            return;
        }

        // 1. Parse Raw Values
        int gunStatus = node.path("gun_status").asInt();
        int errorCode = node.path("error_code").asInt();
        
        // Frontend expects: V, A, kW
        double currentSoc = node.path("current_soc").asDouble();      // %
        double currentVoltage = node.path("current_voltage").asDouble(); // V
        double currentCurrent = node.path("current_current").asDouble(); // A
        double currentPower = node.path("current_power").asDouble();     // kW
        
        double chargeEnergy = node.path("charge_energy").asDouble();     // kWh
        double chargeTimeLength = node.path("charge_time_length").asDouble(); // Seconds?
        int temp = node.path("gun_temperature").asInt();

        // 2. Map Status for Frontend (available, charging, fault, offline)
        String frontendStatus = "offline";
        // Simple mapping logic
        if (errorCode > 0) {
            frontendStatus = "fault"; // Or 'error'
        } else if (gunStatus == 1) { // 1=Standby (Assumption)
            frontendStatus = "online";
        } else if (gunStatus == 2 || gunStatus == 3) { // 2=Charging, 3=Full/Occupied
            frontendStatus = "charging";
        }

        // 3. Log converted data (Simulating a Push)
        log.info("Device Status Update [{}]: Status={} SOC={}% Volt={}V Amp={}A Power={}kW Energy={}kWh Temp={}C",
                deviceId, frontendStatus, currentSoc, currentVoltage, currentCurrent, currentPower, chargeEnergy, temp);
        
        // Update DB
        try {
            stationService.updateStatusByCode(deviceId, frontendStatus, currentVoltage, currentCurrent, currentPower, currentSoc, chargeEnergy, temp);
        } catch (Exception e) {
            log.error("Failed to update db status for device " + deviceId, e);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {}
}