#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
充电桩MQTT数据接收器 - 直接将数据保存到MySQL数据库
支持Linux/Unix环境
"""

import paho.mqtt.client as mqtt
import json
import sys
import os

# 尝试导入MySQL驱动
try:
    import mysql.connector
    from mysql.connector import Error
except ImportError:
    print("❌ 缺少 mysql-connector-python 包")
    print("请运行: pip3 install mysql-connector-python")
    sys.exit(1)

# ========== 配置 ==========
# 您的私有服务器配置
MQTT_BROKER = "129.204.21.66"
MQTT_PORT = 1883
MQTT_USERNAME = "root"
MQTT_PASSWORD = "root1234"
MQTT_TOPIC_STATUS = "device/+/status"
MQTT_TOPIC_RECORD = "device/+/record"

# MySQL配置 - 根据环境调整
DB_CONFIG = {
    "host": os.environ.get("DB_HOST", "localhost"),
    "user": os.environ.get("DB_USER", "root"),
    "password": os.environ.get("DB_PASSWORD", "root"),
    "database": os.environ.get("DB_NAME", "charging_station"),
    "port": int(os.environ.get("DB_PORT", "3306"))
}

print("\n📋 配置信息:")
print(f"  MQTT服务器: {MQTT_BROKER}:{MQTT_PORT}")
print(f"  数据库: {DB_CONFIG['user']}@{DB_CONFIG['host']}:{DB_CONFIG['port']}/{DB_CONFIG['database']}")
print()

# ========== 数据库管理 ==========
class DatabaseManager:
    def __init__(self):
        self.conn = None
        self.connect()
    
    def connect(self):
        """连接到数据库"""
        try:
            self.conn = mysql.connector.connect(**DB_CONFIG)
            print("✅ MySQL数据库连接成功\n")
            return True
        except Error as e:
            print(f"❌ MySQL连接失败: {e}")
            print(f"   请检查数据库配置:")
            for k, v in DB_CONFIG.items():
                if k != 'password':
                    print(f"   - {k}: {v}")
            return False

    def save_charging_record(self, data):
        """保存充电记录到数据库"""
        if not self.conn:
            return

        try:
            cursor = self.conn.cursor()
            query_insert = """
                INSERT INTO charging_record 
                (station_id, station_name, user_id, user_name, user_phone, 
                 start_time, end_time, duration, energy, start_soc, end_soc, 
                 peak_energy, valley_energy, normal_energy, electricity_fee, service_fee, total_fee,
                 status, payment_status, payment_method, payment_time, created_at, updated_at)
                VALUES 
                (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, NOW(), NOW())
            """
            
            # 提取数据，提供默认值防止报错
            values = (
                data.get('station_id', 0),
                data.get('station_name', 'Unknown'),
                data.get('user_id', 0),
                data.get('user_name', 'Unknown'),
                data.get('user_phone', ''),
                data.get('start_time'),
                data.get('end_time'),
                data.get('duration', 0),
                data.get('energy', 0.0),
                data.get('start_soc', 0),
                data.get('end_soc', 0),
                data.get('peak_energy', 0.0),
                data.get('valley_energy', 0.0),
                data.get('normal_energy', 0.0),
                data.get('electricity_fee', 0.0),
                data.get('service_fee', 0.0),
                data.get('total_fee', 0.0),
                data.get('status', 'completed'),
                data.get('payment_status', 'unpaid'),
                data.get('payment_method', ''),
                data.get('payment_time')
            )
            
            cursor.execute(query_insert, values)
            self.conn.commit()
            print(f"📝 [记录] 已保存充电记录 (金额: {data.get('total_fee', 0)}元)")
            cursor.close()
        except Error as e:
            print(f"❌ 保存充电记录失败: {e}")
            if self.conn:
                self.conn.rollback()
    
    def create_or_update_station(self, device_code, data):
        """创建或更新充电桩信息"""
        if not self.conn:
            return
        
        try:
            cursor = self.conn.cursor()
            
            # 检查设备是否存在
            query_check = "SELECT id FROM charging_station WHERE code = %s OR serial_number = %s LIMIT 1"
            cursor.execute(query_check, (device_code, device_code))
            result = cursor.fetchone()
            
            # 提取数据
            power = float(data.get('current_power', 0)) * 0.1
            voltage = float(data.get('current_voltage', 0)) * 0.1
            current = float(data.get('current_current', 0)) * 0.01
            onoff = int(data.get('onoff', -1))
            status_str = self._map_status(onoff)
            
            if result:
                # 更新现有设备
                station_id = result[0]
                query_update = """
                    UPDATE charging_station 
                    SET power = %s, voltage = %s, current = %s, status = %s, updated_at = NOW()
                    WHERE id = %s
                """
                cursor.execute(query_update, (power, voltage, current, status_str, station_id))
                print(f"📊 [更新] 充电桩 {device_code}")
            else:
                # 创建新设备
                device_short = device_code.split(":")[-1] if ":" in device_code else device_code[-8:]
                query_insert = """
                    INSERT INTO charging_station 
                    (code, serial_number, name, type, status, location, manufacturer, model, 
                     power, voltage, current, install_date, created_at, updated_at)
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, CURDATE(), NOW(), NOW())
                """
                cursor.execute(query_insert, (
                    device_code, device_code, f"充电桩-{device_short}", "DC",
                    status_str, "未知位置", "ESP32", "ESP32-S3",
                    power, voltage, current
                ))
                print(f"🆕 [创建] 充电桩 {device_code}")
            
            self.conn.commit()
            cursor.close()
            
            # 打印数据摘要
            print(f"     功率: {power:.1f}kW | 电压: {voltage:.1f}V | 电流: {current:.2f}A | 状态: {status_str}")
            
        except Error as e:
            print(f"❌ 数据库操作失败: {e}")
            if self.conn:
                self.conn.rollback()
    
    @staticmethod
    def _map_status(onoff_value):
        """映射设备状态"""
        status_map = {0: "charging", 1: "online", 2: "offline", 3: "error"}
        return status_map.get(onoff_value, "unknown")
    
    def close(self):
        """关闭数据库连接"""
        if self.conn and self.conn.is_connected():
            self.conn.close()

# ========== MQTT客户端 ==========
db = None
msg_count = 0

def on_connect(client, userdata, flags, rc):
    global msg_count
    msg_count = 0
    if rc == 0:
        print("✅ MQTT已连接到服务器")
        # 订阅状态和记录两个话题
        client.subscribe([(MQTT_TOPIC_STATUS, 1), (MQTT_TOPIC_RECORD, 1)])
        print(f"📡 已订阅话题: \n   - {MQTT_TOPIC_STATUS}\n   - {MQTT_TOPIC_RECORD}\n")
    else:
        print(f"❌ 连接失败，错误代码: {rc}")

def on_disconnect(client, userdata, rc):
    if rc != 0:
        print(f"⚠️  意外断开连接，错误代码: {rc}")

def on_message(client, userdata, msg):
    """处理接收到的消息"""
    global msg_count
    try:
        topic_parts = msg.topic.split("/")
        
        # 兼容两种格式
        if len(topic_parts) == 3 and topic_parts[0] == "device":
             # 格式: device/{code}/status
            device_code = topic_parts[1]
            msg_type = topic_parts[2]
        elif len(topic_parts) >= 4 and topic_parts[1] == "device":
            # 格式: prefix/device/{code}/status
            device_code = topic_parts[2]
            msg_type = topic_parts[3]
        else:
            return

        try:
            payload_str = msg.payload.decode()
            data = json.loads(payload_str)
        except:
            print(f"⚠️ 收到无效JSON数据")
            return

        msg_count += 1
        print(f"\n[{msg_count}] 📨 收到消息 [{msg_type}]: {device_code}")
        
        if db:
            if msg_type == "status":
                db.create_or_update_station(device_code, data)
            elif msg_type == "record":
                db.save_charging_record(data)
    
    except Exception as e:
        print(f"❌ 处理消息失败: {e}")

def main():
    global db
    
    print("=" * 70)
    print("         充电桩MQTT数据接收器 v2.0 (Linux版)")
    print("=" * 70)
    
    # 连接数据库
    db = DatabaseManager()
    if not db or not db.conn:
        print("\n❌ 无法连接到数据库，请检查配置后重试")
        sys.exit(1)
    
    # 创建MQTT客户端
    print(f"🔌 正在连接到MQTT服务器...")
    client = mqtt.Client(mqtt.CallbackAPIVersion.VERSION1)
    client.username_pw_set(MQTT_USERNAME, MQTT_PASSWORD)
    client.on_connect = on_connect
    client.on_disconnect = on_disconnect
    client.on_message = on_message
    
    try:
        client.connect(MQTT_BROKER, MQTT_PORT, keepalive=60)
        print("   正在监听MQTT消息... (按 Ctrl+C 停止)\n")
        client.loop_forever()
    except KeyboardInterrupt:
        print("\n\n⏹️  正在关闭服务...")
    except Exception as e:
        print(f"❌ 错误: {e}")
    finally:
        client.disconnect()
        if db:
            db.close()
        print("✅ 已关闭所有连接")

if __name__ == "__main__":
    main()
