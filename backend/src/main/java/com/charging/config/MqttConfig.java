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
        try {
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            var options = new MqttConnectOptions();
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(30);
            options.setKeepAliveInterval(60);
            client.setCallback(this);
            client.connect(options);
            client.subscribe(topic, 1);
            log.info("MQTT connected successfully to broker: {}, subscribed to topic: {}", broker, topic);
        } catch (MqttException e) {
            log.error("Failed to connect to MQTT broker: {}", broker, e);
            throw e;
        }
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
        log.error("MQTT connection lost: {}", cause.getMessage(), cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            var payload = new String(message.getPayload(), StandardCharsets.UTF_8);
            log.debug("Received MQTT message from topic: {}, payload: {}", topic, payload);
            
            String[] parts = topic.split("/");
            if (parts.length < 4) {
                log.warn("Ignored message from unknown topic format: {}", topic);
                return;
            }
            
            String deviceId = parts[2];
            String msgType = parts[3];

            if ("status".equals(msgType)) {
                parseStatusPayload(deviceId, payload);
            } else if ("controll".equals(msgType)) {
                log.info("Received control reply from device: {}", deviceId);
            } else {
                log.warn("Unknown message type: {} from device: {}", msgType, deviceId);
            }

        } catch (Exception e) {
            log.error("Failed to parse MQTT message from topic: {}", topic, e);
        }
    }

    private void parseStatusPayload(String deviceId, String payload) {
        try {
            var node = objectMapper.readTree(payload);
            
            if (node.has("type") && !"status".equals(node.path("type").asText())) {
                return;
            }

            int gunStatus = node.path("gun_status").asInt();
            int errorCode = node.path("error_code").asInt();
            
            double currentSoc = node.path("current_soc").asDouble();
            double currentVoltage = node.path("current_voltage").asDouble();
            double currentCurrent = node.path("current_current").asDouble();
            double currentPower = node.path("current_power").asDouble();
            double chargeEnergy = node.path("charge_energy").asDouble();
            int temp = node.path("gun_temperature").asInt();

            String frontendStatus = mapDeviceStatus(gunStatus, errorCode);

            log.info("Device [{}] Status Update: Status={}, SOC={}%, Volt={}V, Amp={}A, Power={}kW, Energy={}kWh, Temp={}C",
                    deviceId, frontendStatus, currentSoc, currentVoltage, currentCurrent, currentPower, chargeEnergy, temp);
            
            stationService.updateStatusByCode(deviceId, frontendStatus, currentVoltage, currentCurrent, currentPower, currentSoc, chargeEnergy, temp);
            
        } catch (Exception e) {
            log.error("Failed to parse status payload from device: {}", deviceId, e);
        }
    }

    private String mapDeviceStatus(int gunStatus, int errorCode) {
        if (errorCode > 0) {
            return "fault";
        }
        
        switch (gunStatus) {
            case 1:
                return "online";
            case 2:
            case 3:
                return "charging";
            default:
                return "offline";
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {}
}