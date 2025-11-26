# 充电桩管理系统 API 文档

## 📋 文档说明

本文档详细列出了充电桩管理系统所有前端页面所需的后端 API 接口规范。

**基础信息：**
- 基础 URL: `http://localhost:8080/api`
- 认证方式: JWT Token (Header: `Authorization: Bearer {token}`)
- 响应格式: JSON
- 字符编码: UTF-8

**统一响应格式：**
```json
{
  "code": 200,           // 状态码: 200成功, 400客户端错误, 500服务端错误
  "message": "success",  // 响应消息
  "data": {}            // 响应数据
}
```

---

## 🔐 1. 认证模块 (Authentication)

### 1.1 用户登录
```
POST /auth/login
```

**请求体：**
```json
{
  "username": "admin",
  "password": "123456"
}
```

**响应：**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "username": "admin",
      "name": "张三",
      "role": "系统管理员",
      "email": "admin@charging.com",
      "avatar": "https://api.dicebear.com/7.x/avataaars/svg?seed=admin"
    }
  }
}
```

### 1.2 用户登出
```
POST /auth/logout
```

**响应：**
```json
{
  "code": 200,
  "message": "退出成功"
}
```

### 1.3 刷新 Token
```
POST /auth/refresh
```

**请求体：**
```json
{
  "refreshToken": "xxx"
}
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "token": "new_token_xxx"
  }
}
```

---

## ⚡ 2. 充电桩记录模块 (Dashboard)

### 2.1 获取充电桩概览统计
```
GET /dashboard/stats
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "totalStations": 156,
    "activeStations": 142,
    "totalRevenue": 45678.50,
    "todayCharging": 89
  }
}
```

### 2.2 获取充电记录列表
```
GET /dashboard/records?page=1&size=10&status=charging&startDate=2024-01-01&endDate=2024-12-31
```

**查询参数：**
- `page`: 页码 (默认: 1)
- `size`: 每页数量 (默认: 10)
- `status`: 状态过滤 (all/charging/completed/stopped)
- `startDate`: 开始日期
- `endDate`: 结束日期

**响应：**
```json
{
  "code": 200,
  "data": {
    "total": 1234,
    "page": 1,
    "size": 10,
    "records": [
      {
        "id": 1,
        "stationId": "CS001",
        "stationName": "充电桩001",
        "userName": "张三",
        "userPhone": "138****5678",
        "startTime": "2024-03-20 08:30:00",
        "endTime": "2024-03-20 10:15:00",
        "duration": 105,
        "energy": 58.5,
        "cost": 87.50,
        "status": "completed"
      }
    ]
  }
}
```

### 2.3 获取实时充电数据
```
WebSocket /ws/charging/realtime
```

**消息格式：**
```json
{
  "type": "update",
  "data": {
    "stationId": "CS001",
    "power": 45.2,
    "voltage": 380,
    "current": 32.5,
    "energy": 12.8,
    "timestamp": "2024-03-20T10:30:00Z"
  }
}
```

---

## 💰 3. 充电资费设置模块 (Settings)

### 3.1 获取资费配置
```
GET /settings/tariff
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "peakPrice": 1.50,
    "normalPrice": 1.00,
    "valleyPrice": 0.60,
    "serviceFee": 0.80,
    "peakHours": "08:00-11:00,18:00-22:00",
    "valleyHours": "23:00-07:00"
  }
}
```

### 3.2 更新资费配置
```
PUT /settings/tariff
```

**请求体：**
```json
{
  "peakPrice": 1.50,
  "normalPrice": 1.00,
  "valleyPrice": 0.60,
  "serviceFee": 0.80,
  "peakHours": "08:00-11:00,18:00-22:00",
  "valleyHours": "23:00-07:00"
}
```

**响应：**
```json
{
  "code": 200,
  "message": "资费配置更新成功"
}
```

### 3.3 获取充电模式配置
```
GET /settings/charging-modes
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "modes": [
      {
        "id": 1,
        "name": "快速充电",
        "power": 120,
        "enabled": true
      },
      {
        "id": 2,
        "name": "标准充电",
        "power": 60,
        "enabled": true
      },
      {
        "id": 3,
        "name": "慢速充电",
        "power": 30,
        "enabled": true
      }
    ]
  }
}
```

### 3.4 更新充电模式
```
PUT /settings/charging-modes/{id}
```

**请求体：**
```json
{
  "name": "快速充电",
  "power": 120,
  "enabled": true
}
```

---

## 🔌 4. 充电桩管理模块 (Charging)

### 4.1 获取充电桩列表
```
GET /charging/stations?page=1&size=10&status=online&search=CS001
```

**查询参数：**
- `page`: 页码
- `size`: 每页数量
- `status`: 状态 (all/online/offline/charging/fault)
- `search`: 搜索关键词

**响应：**
```json
{
  "code": 200,
  "data": {
    "total": 156,
    "page": 1,
    "size": 10,
    "stations": [
      {
        "id": 1,
        "stationId": "CS001",
        "name": "充电桩001",
        "location": "北京市朝阳区xxx",
        "type": "快充",
        "power": 120,
        "status": "online",
        "voltage": 380,
        "current": 0,
        "temperature": 25,
        "totalEnergy": 12580.5,
        "totalRevenue": 18870.75,
        "createTime": "2024-01-01 00:00:00",
        "updateTime": "2024-03-20 10:30:00"
      }
    ]
  }
}
```

### 4.2 获取充电桩详情
```
GET /charging/stations/{id}
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "stationId": "CS001",
    "name": "充电桩001",
    "location": "北京市朝阳区xxx",
    "type": "快充",
    "power": 120,
    "status": "online",
    "voltage": 380,
    "current": 0,
    "temperature": 25,
    "totalEnergy": 12580.5,
    "totalRevenue": 18870.75,
    "todayEnergy": 158.5,
    "todayRevenue": 237.75,
    "utilizationRate": 78.5,
    "createTime": "2024-01-01 00:00:00"
  }
}
```

### 4.3 创建充电桩
```
POST /charging/stations
```

**请求体：**
```json
{
  "stationId": "CS999",
  "name": "充电桩999",
  "location": "北京市朝阳区xxx",
  "type": "快充",
  "power": 120
}
```

**响应：**
```json
{
  "code": 200,
  "message": "充电桩创建成功",
  "data": {
    "id": 999
  }
}
```

### 4.4 更新充电桩
```
PUT /charging/stations/{id}
```

**请求体：**
```json
{
  "name": "充电桩001-更新",
  "location": "北京市朝阳区xxx新地址",
  "power": 150
}
```

### 4.5 删除充电桩
```
DELETE /charging/stations/{id}
```

### 4.6 批量删除充电桩
```
DELETE /charging/stations/batch
```

**请求体：**
```json
{
  "ids": [1, 2, 3]
}
```

### 4.7 启用/禁用充电桩
```
PUT /charging/stations/{id}/status
```

**请求体：**
```json
{
  "enabled": true
}
```

### 4.8 导出充电桩数据
```
GET /charging/stations/export?format=excel
```

**查询参数：**
- `format`: 导出格式 (excel/csv)

---

## 📊 5. 充电设备状态模块 (Device)

### 5.1 获取设备监控列表
```
GET /devices/monitor?status=online
```

**查询参数：**
- `status`: 状态过滤 (all/online/offline/charging/fault)

**响应：**
```json
{
  "code": 200,
  "data": {
    "devices": [
      {
        "id": 1,
        "stationId": "CS001",
        "name": "充电桩001",
        "status": "charging",
        "power": 45.2,
        "voltage": 380,
        "current": 32.5,
        "temperature": 42,
        "soc": 65,
        "energy": 12.8
      }
    ]
  }
}
```

### 5.2 获取设备详细信息
```
GET /devices/{id}/detail
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "stationId": "CS001",
    "name": "充电桩001",
    "status": "charging",
    "power": 45.2,
    "voltage": 380,
    "current": 32.5,
    "temperature": 42,
    "soc": 65,
    "energy": 12.8,
    "startTime": "2024-03-20 08:30:00",
    "duration": 45,
    "cost": 28.50,
    "user": {
      "name": "张三",
      "phone": "138****5678"
    },
    "vehicle": {
      "plate": "京A12345",
      "model": "特斯拉 Model 3"
    }
  }
}
```

### 5.3 设备实时数据流
```
WebSocket /ws/devices/{id}/stream
```

---

## 📝 6. 充电记录查看模块 (Records)

### 6.1 获取充电记录
```
GET /records?page=1&size=20&status=completed&userId=1&stationId=CS001
```

**查询参数：**
- `page`: 页码
- `size`: 每页数量
- `status`: 状态 (all/charging/completed/stopped)
- `userId`: 用户ID
- `stationId`: 充电桩ID
- `startDate`: 开始日期
- `endDate`: 结束日期

**响应：**
```json
{
  "code": 200,
  "data": {
    "total": 1234,
    "page": 1,
    "size": 20,
    "records": [
      {
        "id": 1,
        "orderId": "ORD20240320001",
        "stationId": "CS001",
        "stationName": "充电桩001",
        "userId": 123,
        "userName": "张三",
        "userPhone": "138****5678",
        "startTime": "2024-03-20 08:30:00",
        "endTime": "2024-03-20 10:15:00",
        "duration": 105,
        "energy": 58.5,
        "cost": 87.50,
        "paymentMethod": "微信支付",
        "status": "completed"
      }
    ]
  }
}
```

### 6.2 获取记录详情
```
GET /records/{id}
```

### 6.3 导出充电记录
```
GET /records/export?format=excel&startDate=2024-01-01&endDate=2024-12-31
```

---

## 👥 7. 用户信息管理模块 (User)

### 7.1 获取用户列表
```
GET /users?page=1&size=20&role=user&status=active&search=张三
```

**查询参数：**
- `page`: 页码
- `size`: 每页数量
- `role`: 角色 (all/admin/user/operator)
- `status`: 状态 (all/active/inactive)
- `search`: 搜索关键词

**响应：**
```json
{
  "code": 200,
  "data": {
    "total": 586,
    "page": 1,
    "size": 20,
    "users": [
      {
        "id": 1,
        "username": "zhangsan",
        "name": "张三",
        "phone": "13800138000",
        "email": "zhangsan@example.com",
        "role": "user",
        "status": "active",
        "balance": 1280.50,
        "totalCharging": 45,
        "totalEnergy": 1250.5,
        "totalCost": 1875.75,
        "registerTime": "2024-01-15 10:30:00",
        "lastLoginTime": "2024-03-20 08:00:00"
      }
    ]
  }
}
```

### 7.2 获取用户详情
```
GET /users/{id}
```

### 7.3 创建用户
```
POST /users
```

**请求体：**
```json
{
  "username": "newuser",
  "name": "新用户",
  "phone": "13900139000",
  "email": "newuser@example.com",
  "password": "123456",
  "role": "user"
}
```

### 7.4 更新用户
```
PUT /users/{id}
```

**请求体：**
```json
{
  "name": "张三",
  "phone": "13800138000",
  "email": "zhangsan@example.com",
  "role": "user"
}
```

### 7.5 删除用户
```
DELETE /users/{id}
```

### 7.6 批量删除用户
```
DELETE /users/batch
```

**请求体：**
```json
{
  "ids": [1, 2, 3]
}
```

### 7.7 重置用户密码
```
PUT /users/{id}/reset-password
```

**请求体：**
```json
{
  "newPassword": "123456"
}
```

### 7.8 用户充值
```
POST /users/{id}/recharge
```

**请求体：**
```json
{
  "amount": 100.00,
  "paymentMethod": "wechat"
}
```

### 7.9 启用/禁用用户
```
PUT /users/{id}/status
```

**请求体：**
```json
{
  "status": "active"
}
```

---

## 💵 8. 充电费用统计模块 (Statistics)

### 8.1 获取费用统计概览
```
GET /statistics/overview?startDate=2024-01-01&endDate=2024-12-31
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "totalRevenue": 125680.50,
    "totalEnergy": 85420.30,
    "totalOrders": 3456,
    "avgOrderAmount": 36.35
  }
}
```

### 8.2 获取收入趋势数据
```
GET /statistics/revenue-trend?startDate=2024-01-01&endDate=2024-12-31&period=day
```

**查询参数：**
- `startDate`: 开始日期
- `endDate`: 结束日期
- `period`: 统计周期 (day/week/month)

**响应：**
```json
{
  "code": 200,
  "data": {
    "dates": ["01-01", "01-02", "01-03", "..."],
    "revenues": [1250.50, 1380.80, 1420.30, "..."],
    "energies": [850.2, 920.5, 950.8, "..."]
  }
}
```

### 8.3 获取充电桩收入排名
```
GET /statistics/station-ranking?startDate=2024-01-01&endDate=2024-12-31&limit=10
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "ranking": [
      {
        "rank": 1,
        "stationId": "CS001",
        "stationName": "充电桩001",
        "revenue": 15680.50,
        "energy": 10520.30,
        "orders": 456
      }
    ]
  }
}
```

### 8.4 获取用户消费排名
```
GET /statistics/user-ranking?startDate=2024-01-01&endDate=2024-12-31&limit=10
```

### 8.5 获取费用明细记录
```
GET /statistics/details?page=1&size=20&startDate=2024-01-01&endDate=2024-12-31
```

### 8.6 导出统计报表
```
GET /statistics/export?format=excel&startDate=2024-01-01&endDate=2024-12-31&type=revenue
```

---

## 🏥 9. 系统健康巡查模块 (System)

### 9.1 获取系统状态
```
GET /system/status
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "status": "healthy",
    "uptime": 1234567,
    "version": "1.0.0",
    "environment": "production"
  }
}
```

### 9.2 获取系统指标
```
GET /system/metrics
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "cpu": {
      "usage": 45.2,
      "cores": 8
    },
    "memory": {
      "used": 4096,
      "total": 16384,
      "usage": 25.0
    },
    "disk": {
      "used": 500,
      "total": 1000,
      "usage": 50.0
    },
    "network": {
      "inbound": 1024,
      "outbound": 512
    }
  }
}
```

### 9.3 获取服务列表
```
GET /system/services
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "services": [
      {
        "id": 1,
        "name": "充电服务",
        "status": "running",
        "uptime": 345600,
        "requests": 125680,
        "errors": 12
      }
    ]
  }
}
```

### 9.4 获取数据库统计
```
GET /system/database
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "connections": 25,
    "maxConnections": 100,
    "tableCount": 15,
    "totalSize": 2048,
    "slowQueries": 5
  }
}
```

### 9.5 获取系统日志
```
GET /system/logs?page=1&size=50&level=error&startDate=2024-03-20
```

**查询参数：**
- `page`: 页码
- `size`: 每页数量
- `level`: 日志级别 (all/info/warning/error)
- `startDate`: 开始日期
- `endDate`: 结束日期

**响应：**
```json
{
  "code": 200,
  "data": {
    "total": 1234,
    "page": 1,
    "size": 50,
    "logs": [
      {
        "id": 1,
        "level": "error",
        "message": "数据库连接超时",
        "module": "database",
        "timestamp": "2024-03-20 10:30:00",
        "details": "..."
      }
    ]
  }
}
```

### 9.6 获取告警信息
```
GET /system/alerts?status=active
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "alerts": [
      {
        "id": 1,
        "level": "critical",
        "title": "充电桩CS003离线",
        "message": "充电桩CS003已离线超过30分钟",
        "status": "active",
        "createTime": "2024-03-20 10:00:00"
      }
    ]
  }
}
```

### 9.7 重启服务
```
POST /system/services/{id}/restart
```

### 9.8 清理缓存
```
POST /system/cache/clear
```

---

## 🔔 10. 通知中心模块 (Notifications)

### 10.1 获取通知列表
```
GET /notifications?page=1&size=20&type=all&status=unread
```

**查询参数：**
- `page`: 页码
- `size`: 每页数量
- `type`: 类型 (all/system/device/user)
- `status`: 状态 (all/unread/read)

**响应：**
```json
{
  "code": 200,
  "data": {
    "total": 156,
    "unreadCount": 8,
    "page": 1,
    "size": 20,
    "notifications": [
      {
        "id": 1,
        "type": "success",
        "category": "device",
        "title": "充电完成",
        "message": "充电桩#001 已完成充电，本次充电58.5kWh，费用¥87.5",
        "isRead": false,
        "createTime": "2024-03-20 10:25:00"
      }
    ]
  }
}
```

### 10.2 获取未读通知数量
```
GET /notifications/unread-count
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "total": 8,
    "system": 2,
    "device": 4,
    "user": 2
  }
}
```

### 10.3 标记通知为已读
```
PUT /notifications/{id}/read
```

### 10.4 标记所有通知为已读
```
PUT /notifications/read-all
```

### 10.5 删除通知
```
DELETE /notifications/{id}
```

### 10.6 获取通知设置
```
GET /notifications/settings
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "systemNotify": true,
    "deviceNotify": true,
    "userNotify": true,
    "desktopNotify": false,
    "emailNotify": false,
    "soundNotify": true
  }
}
```

### 10.7 更新通知设置
```
PUT /notifications/settings
```

**请求体：**
```json
{
  "systemNotify": true,
  "deviceNotify": true,
  "userNotify": true,
  "desktopNotify": false
}
```

---

## 👤 11. 个人中心模块 (Profile)

### 11.1 获取个人信息
```
GET /profile
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "username": "admin",
    "name": "张三",
    "phone": "13800138000",
    "email": "admin@charging.com",
    "department": "技术部",
    "position": "系统管理员",
    "avatar": "https://api.dicebear.com/7.x/avataaars/svg?seed=admin",
    "role": "系统管理员",
    "joinDate": "2024-01-01",
    "loginDays": 28,
    "totalOperations": 1580,
    "lastLoginTime": "2024-03-20 08:00:00"
  }
}
```

### 11.2 更新个人信息
```
PUT /profile
```

**请求体：**
```json
{
  "name": "张三",
  "phone": "13800138000",
  "email": "admin@charging.com",
  "department": "技术部",
  "position": "系统管理员"
}
```

### 11.3 上传头像
```
POST /profile/avatar
Content-Type: multipart/form-data
```

**请求体：**
- `file`: 图片文件 (支持 jpg, png, 最大 2MB)

**响应：**
```json
{
  "code": 200,
  "message": "头像上传成功",
  "data": {
    "avatarUrl": "https://cdn.example.com/avatars/user_1.jpg"
  }
}
```

### 11.4 修改密码
```
PUT /profile/password
```

**请求体：**
```json
{
  "currentPassword": "old_password",
  "newPassword": "new_password"
}
```

### 11.5 获取操作记录
```
GET /profile/activities?page=1&size=10
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "total": 1580,
    "page": 1,
    "size": 10,
    "activities": [
      {
        "id": 1,
        "action": "编辑充电桩",
        "target": "充电桩CS001",
        "type": "edit",
        "timestamp": "2024-03-20 10:30:00"
      }
    ]
  }
}
```

### 11.6 获取统计数据
```
GET /profile/stats
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "loginDays": 28,
    "totalOperations": 1580,
    "unreadMessages": 8,
    "todayOperations": 12
  }
}
```

---

## ⚙️ 12. 系统设置模块 (Settings System)

### 12.1 获取系统设置
```
GET /settings/system
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "general": {
      "language": "zh-CN",
      "timezone": "UTC+8",
      "theme": "light",
      "autoRefresh": true,
      "animations": true
    },
    "notifications": {
      "systemNotify": true,
      "deviceNotify": true,
      "userNotify": true,
      "desktopNotify": false,
      "soundNotify": true,
      "emailNotify": false
    },
    "security": {
      "twoFactor": false,
      "loginProtection": true,
      "sessionTimeout": 120,
      "ipWhitelist": false,
      "operationLog": true
    },
    "data": {
      "autoBackup": true,
      "backupFrequency": "daily",
      "dataRetention": 90,
      "dataCompression": true
    }
  }
}
```

### 12.2 更新系统设置
```
PUT /settings/system
```

**请求体：**
```json
{
  "general": {
    "language": "zh-CN",
    "timezone": "UTC+8",
    "theme": "light"
  }
}
```

### 12.3 立即备份
```
POST /settings/backup
```

**响应：**
```json
{
  "code": 200,
  "message": "备份成功",
  "data": {
    "backupId": "backup_20240320_103000",
    "size": 2048576,
    "timestamp": "2024-03-20 10:30:00"
  }
}
```

### 12.4 导出数据
```
GET /settings/export?format=json&tables=all
```

### 12.5 清除缓存
```
POST /settings/cache/clear
```

### 12.6 重置设置
```
POST /settings/reset
```

---

## 📊 13. 充电使用统计模块 (Usage)

### 13.1 获取使用统计
```
GET /usage/stats?startDate=2024-01-01&endDate=2024-12-31
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "totalEnergy": 85420.30,
    "totalSessions": 3456,
    "avgDuration": 65,
    "peakHour": "18:00-19:00"
  }
}
```

### 13.2 获取时段使用分布
```
GET /usage/distribution?date=2024-03-20
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "hourly": [
      {
        "hour": "00:00",
        "sessions": 5,
        "energy": 125.5
      }
    ]
  }
}
```

---

## 📤 14. 通用接口

### 14.1 文件上传
```
POST /upload
Content-Type: multipart/form-data
```

**请求体：**
- `file`: 文件
- `type`: 文件类型 (avatar/document/image)

**响应：**
```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "url": "https://cdn.example.com/files/xxx.jpg",
    "size": 1024576,
    "filename": "xxx.jpg"
  }
}
```

### 14.2 获取系统配置
```
GET /config
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "systemName": "充电桩管理系统",
    "version": "1.0.0",
    "copyright": "© 2024 充电桩管理系统",
    "supportEmail": "support@charging.com"
  }
}
```

---

## 📋 状态码说明

| 状态码 | 说明 | 处理方式 |
|--------|------|----------|
| 200 | 成功 | 正常处理 |
| 400 | 请求参数错误 | 检查请求参数 |
| 401 | 未授权 | 跳转到登录页 |
| 403 | 无权限 | 显示权限不足提示 |
| 404 | 资源不存在 | 显示404页面 |
| 500 | 服务器错误 | 显示错误提示，联系管理员 |
| 503 | 服务不可用 | 显示维护提示 |

---

## 🔒 安全说明

### Token 认证
所有需要认证的接口都需要在请求头中携带 JWT Token：
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Token 刷新机制
- Token 有效期：2小时
- 在 Token 过期前 10 分钟，前端应自动调用刷新接口
- 如果 Token 已过期，返回 401，需重新登录

### 权限控制
不同角色拥有不同的接口访问权限：
- **系统管理员**: 所有接口
- **运营人员**: 充电桩管理、统计查看、用户管理
- **普通用户**: 个人信息、充电记录查看

---

## 🌐 WebSocket 接口

### 连接地址
```
ws://localhost:8080/ws
```

### 心跳机制
- 客户端每 30 秒发送一次 ping
- 服务端响应 pong
- 超时断开重连

### 订阅主题
```json
{
  "action": "subscribe",
  "topics": ["charging.realtime", "devices.status", "notifications"]
}
```

---

## 📝 更新日志

### v1.0.0 (2024-03-20)
- 初始版本
- 完成所有核心模块 API 设计
- 支持 13 个功能模块
- 完整的认证和权限控制

---

## 📞 技术支持

如有问题，请联系：
- Email: dev@charging.com
- 技术文档: https://docs.charging.com
- GitHub: https://github.com/charging-system

---

**文档版本**: v1.0.0  
**最后更新**: 2024-03-20  
**维护者**: 技术团队
