#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
充电桩MQTT数据接收器 - 直接将数据保存到MySQL数据库
用于在后端未运行时接收设备数据
"""

import paho.mqtt.client as mqtt
import json
import mysql.connector
from datetime import datetime
import time
import sys

# ========== 配置 ==========
MQTT_BROKER = "129.204.21.66"
MQTT_PORT = 1883
MQTT_USERNAME = "charge"
MQTT_PASSWORD = "123456"
MQTT_TOPIC = "/device/+/status"

DB_HOST = "localhost"
DB_USER = "root"
DB_PASSWORD = "root"
DB_NAME = "charging_station"

# ========== MySQL连接 ==========
class DatabaseManager:
    def __init__(self):
        self.conn = None
        self.connect()
    
    def connect(self):
        try:
            self.conn = mysql.connector.connect(
                host=DB_HOST,
                user=DB_USER,
                password=DB_PASSWORD,
                database=DB_NAME
            )
            print(f"✅ MySQL数据库连接成功")
            return True
        except Exception as e:
            print(f"❌ MySQL连接失败: {e}")
            return False
    
    def create_or_update_station(self, device_code, data):
        """创建或更新充电桩信息"""
        try:
            cursor = self.conn.cursor()
            
            # 检查设备是否存在
            cursor.execute(
                "SELECT id FROM charging_station WHERE code = %s OR serial_number = %s",
                (device_code, device_code)
            )
            result = cursor.fetchone()
            
            if result:
                # 更新现有设备
                station_id = result[0]
                cursor.execute("""
                    UPDATE charging_station 
                    SET 
                        power = %s,
                        voltage = %s,
                        current = %s,
                        status = %s,
                        updated_at = NOW()
                    WHERE id = %s
                """, (
                    float(data.get('current_power', 0)) * 0.1,  # 0.1kW -> kW
                    float(data.get('current_voltage', 0)) * 0.1,  # 0.1V -> V
                    float(data.get('current_current', 0)) * 0.01,  # 0.01A -> A
                    self._map_status(data.get('onoff', -1)),
                    station_id
                ))
                print(f"📊 更新充电桩 [{device_code}]")
            else:
                # 创建新设备
                device_short_name = device_code.split(":")[-1] if ":" in device_code else device_code[-8:]
                cursor.execute("""
                    INSERT INTO charging_station 
                    (code, serial_number, name, type, status, location, manufacturer, model, power, voltage, current, created_at, updated_at)
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, NOW(), NOW())
                """, (
                    device_code,
                    device_code,
                    f"充电桩-{device_short_name}",
                    "DC",
                    self._map_status(data.get('onoff', -1)),
                    "未知位置",
                    "ESP32",
                    "ESP32-S3",
                    float(data.get('current_power', 0)) * 0.1,
                    float(data.get('current_voltage', 0)) * 0.1,
                    float(data.get('current_current', 0)) * 0.01
                ))
                print(f"🆕 创建新充电桩 [{device_code}]")
            
            self.conn.commit()
            cursor.close()
            
        except Exception as e:
            print(f"❌ 数据库操作失败: {e}")
            self.conn.rollback()
    
    @staticmethod
    def _map_status(onoff_value):
        """映射设备状态"""
        status_map = {
            0: "charging",
            1: "idle", 
            2: "offline",
            3: "error"
        }
        return status_map.get(onoff_value, "unknown")

# ========== MQTT客户端 ==========
db = None

def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print(f"✅ MQTT已连接到服务器")
        client.subscribe(MQTT_TOPIC, qos=1)
        print(f"📡 已订阅话题: {MQTT_TOPIC}")
    else:
        print(f"❌ 连接失败，错误代码: {rc}")

def on_disconnect(client, userdata, rc):
    if rc != 0:
        print(f"⚠️  意外断开连接，错误代码: {rc}")

def on_message(client, userdata, msg):
    """处理接收到的消息"""
    try:
        topic_parts = msg.topic.split("/")
        if len(topic_parts) >= 4:
            device_code = topic_parts[2]
            msg_type = topic_parts[3]
            
            if msg_type == "status":
                data = json.loads(msg.payload.decode())
                print(f"\n📡 收到数据 [{device_code}]:")
                print(f"   功率: {data.get('current_power', 0) * 0.1:.1f} kW")
                print(f"   电压: {data.get('current_voltage', 0) * 0.1:.1f} V")
                print(f"   电流: {data.get('current_current', 0) * 0.01:.2f} A")
                print(f"   状态: {data.get('onoff', -1)}")
                
                # 保存到数据库
                if db:
                    db.create_or_update_station(device_code, data)
    
    except Exception as e:
        print(f"❌ 处理消息失败: {e}")

# ========== 主程序 ==========
def main():
    global db
    
    print("\n" + "="*60)
    print("充电桩MQTT数据接收器 v1.0")
    print("="*60 + "\n")
    
    # 连接数据库
    db = DatabaseManager()
    if not db or not db.conn:
        print("❌ 无法连接到数据库，退出程序")
        return
    
    # 创建MQTT客户端
    print(f"🔌 正在连接到MQTT服务器: {MQTT_BROKER}:{MQTT_PORT}")
    client = mqtt.Client(mqtt.CallbackAPIVersion.VERSION1)
    client.username_pw_set(MQTT_USERNAME, MQTT_PASSWORD)
    client.on_connect = on_connect
    client.on_disconnect = on_disconnect
    client.on_message = on_message
    
    try:
        client.connect(MQTT_BROKER, MQTT_PORT, keepalive=60)
        client.loop_forever()
    except KeyboardInterrupt:
        print("\n\n⏹️  正在关闭...")
    except Exception as e:
        print(f"❌ 错误: {e}")
    finally:
        client.disconnect()
        if db and db.conn:
            db.conn.close()
        print("✅ 已关闭连接")

if __name__ == "__main__":
    main()
