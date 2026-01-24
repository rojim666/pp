-- 添加费率配置表的缺失字段
-- 执行此脚本前请备份数据库

USE charging_station;

-- 添加停车费字段
ALTER TABLE `tariff_config` 
ADD COLUMN `parking_fee` DECIMAL(10,2) DEFAULT 5.00 COMMENT '停车费(元/小时)' 
AFTER `service_fee`;

-- 添加最大充电时长字段
ALTER TABLE `tariff_config` 
ADD COLUMN `max_charging_hours` DECIMAL(5,1) DEFAULT 4.0 COMMENT '单次充电时长限制(小时)' 
AFTER `parking_fee`;

-- 添加免费停车时长字段
ALTER TABLE `tariff_config` 
ADD COLUMN `free_parking_minutes` INT DEFAULT 15 COMMENT '免费停车时长(分钟)' 
AFTER `max_charging_hours`;

-- 添加最低充电金额字段
ALTER TABLE `tariff_config` 
ADD COLUMN `min_charge_fee` DECIMAL(10,2) DEFAULT 1.00 COMMENT '最低充电金额(元)' 
AFTER `free_parking_minutes`;

-- 添加最大充电功率字段
ALTER TABLE `tariff_config` 
ADD COLUMN `max_power` INT DEFAULT 120 COMMENT '最大充电功率(kW)' 
AFTER `min_charge_fee`;

-- 查看表结构
DESCRIBE `tariff_config`;
