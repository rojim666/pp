-- 充电桩管理系统数据库
CREATE DATABASE IF NOT EXISTS charging_station DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE charging_station;

-- 用户表
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码(加密)',
    `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `real_name` VARCHAR(50) COMMENT '真实姓名',
    `avatar` VARCHAR(255) COMMENT '头像URL',
    `id_card` VARCHAR(18) COMMENT '身份证号',
    `role` VARCHAR(20) NOT NULL DEFAULT 'user' COMMENT '角色: admin/operator/user',
    `balance` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
    `total_charging_count` INT NOT NULL DEFAULT 0 COMMENT '累计充电次数',
    `total_charging_energy` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '累计充电量(kWh)',
    `total_charging_fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '累计充电费用',
    `vehicle_plate` VARCHAR(20) COMMENT '车牌号',
    `vehicle_model` VARCHAR(50) COMMENT '车型',
    `vehicle_brand` VARCHAR(50) COMMENT '品牌',
    `is_vip` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否VIP: 0-否 1-是',
    `vip_expire_date` DATE COMMENT 'VIP到期日期',
    `status` VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '状态: active/inactive/blocked',
    `last_login_time` DATETIME COMMENT '最后登录时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 充电桩表
CREATE TABLE `charging_station` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '充电桩名称',
    `code` VARCHAR(50) NOT NULL COMMENT '充电桩编号',
    `type` VARCHAR(20) NOT NULL COMMENT '类型: ac/dc/super',
    `status` VARCHAR(20) NOT NULL DEFAULT 'online' COMMENT '状态: online/offline/charging/maintenance/error',
    `power` DECIMAL(10,2) NOT NULL COMMENT '功率(kW)',
    `location` VARCHAR(200) NOT NULL COMMENT '位置',
    `latitude` DECIMAL(10,6) COMMENT '纬度',
    `longitude` DECIMAL(10,6) COMMENT '经度',
    `voltage` DECIMAL(10,2) NOT NULL COMMENT '电压(V)',
    `current` DECIMAL(10,2) NOT NULL COMMENT '电流(A)',
    `temperature` DECIMAL(5,2) COMMENT '温度(°C)',
    `usage_count` INT NOT NULL DEFAULT 0 COMMENT '使用次数',
    `total_energy` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '总充电量(kWh)',
    `total_revenue` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '总收入(元)',
    `last_maintenance_date` DATE COMMENT '最后维护日期',
    `install_date` DATE NOT NULL COMMENT '安装日期',
    `manufacturer` VARCHAR(100) COMMENT '制造商',
    `model` VARCHAR(100) COMMENT '型号',
    `serial_number` VARCHAR(100) COMMENT '序列号',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`),
    KEY `idx_status` (`status`),
    KEY `idx_type` (`type`),
    KEY `idx_location` (`location`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充电桩表';

-- 充电记录表
CREATE TABLE `charging_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `station_id` BIGINT NOT NULL COMMENT '充电桩ID',
    `station_name` VARCHAR(100) NOT NULL COMMENT '充电桩名称',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `user_name` VARCHAR(50) NOT NULL COMMENT '用户名',
    `user_phone` VARCHAR(20) NOT NULL COMMENT '用户手机号',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME COMMENT '结束时间',
    `duration` INT COMMENT '时长(分钟)',
    `energy` DECIMAL(10,2) COMMENT '充电量(kWh)',
    `start_soc` INT COMMENT '开始电量(%)',
    `end_soc` INT COMMENT '结束电量(%)',
    `peak_energy` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '峰时电量(kWh)',
    `valley_energy` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '谷时电量(kWh)',
    `normal_energy` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '平时电量(kWh)',
    `electricity_fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '电费(元)',
    `service_fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '服务费(元)',
    `total_fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '总费用(元)',
    `status` VARCHAR(20) NOT NULL DEFAULT 'charging' COMMENT '状态: charging/completed/cancelled/failed',
    `payment_status` VARCHAR(20) NOT NULL DEFAULT 'unpaid' COMMENT '支付状态: unpaid/paid/refunded',
    `payment_method` VARCHAR(20) COMMENT '支付方式: wechat/alipay/card',
    `payment_time` DATETIME COMMENT '支付时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_station_id` (`station_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_start_time` (`start_time`),
    KEY `idx_payment_status` (`payment_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充电记录表';

-- 费率配置表
CREATE TABLE `tariff_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '费率名称',
    `peak_price` DECIMAL(10,2) NOT NULL COMMENT '峰时电价(元/kWh)',
    `valley_price` DECIMAL(10,2) NOT NULL COMMENT '谷时电价(元/kWh)',
    `normal_price` DECIMAL(10,2) NOT NULL COMMENT '平时电价(元/kWh)',
    `service_fee` DECIMAL(10,2) NOT NULL COMMENT '服务费(元/kWh)',
    `peak_hours` VARCHAR(200) COMMENT '峰时时段(JSON)',
    `valley_hours` VARCHAR(200) COMMENT '谷时时段(JSON)',
    `is_default` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否默认',
    `effective_date` DATE NOT NULL COMMENT '生效日期',
    `expiry_date` DATE COMMENT '失效日期',
    `description` VARCHAR(500) COMMENT '描述',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_is_default` (`is_default`),
    KEY `idx_effective_date` (`effective_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='费率配置表';

-- 系统日志表
CREATE TABLE `system_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `type` VARCHAR(20) NOT NULL COMMENT '类型: info/warning/error/success',
    `module` VARCHAR(50) NOT NULL COMMENT '模块',
    `action` VARCHAR(50) NOT NULL COMMENT '操作',
    `message` VARCHAR(500) NOT NULL COMMENT '消息',
    `user_id` BIGINT COMMENT '用户ID',
    `user_name` VARCHAR(50) COMMENT '用户名',
    `details` TEXT COMMENT '详细信息(JSON)',
    `ip_address` VARCHAR(50) COMMENT 'IP地址',
    `user_agent` VARCHAR(500) COMMENT '用户代理',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_type` (`type`),
    KEY `idx_module` (`module`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志表';

-- 插入默认管理员账户 (密码: admin123)
INSERT INTO `sys_user` (`username`, `password`, `phone`, `real_name`, `role`, `balance`) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138000', '系统管理员', 'admin', 10000.00);

-- 插入默认费率配置
INSERT INTO `tariff_config` (`name`, `peak_price`, `valley_price`, `normal_price`, `service_fee`, `peak_hours`, `valley_hours`, `is_default`, `effective_date`, `description`)
VALUES ('标准费率', 1.20, 0.60, 0.80, 0.80, '["08:00-12:00","18:00-22:00"]', '["00:00-06:00"]', 1, '2024-01-01', '工作日标准费率');
