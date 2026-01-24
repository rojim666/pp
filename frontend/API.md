# 充电桩管理系统 API 接口文档.....

## 📋 基础信息

- **Base URL**: `http://localhost:8080/api`
- **API版本**: v1.0
- **认证方式**: JWT Bearer Token
- **数据格式**: JSON
- **字符编码**: UTF-8

## 🔐 认证说明

### 请求头格式

```http
Authorization: Bearer {token}
Content-Type: application/json
```

### 获取Token

通过登录接口获取JWT Token，在后续请求的Header中携带。

**Token有效期**: 7天

## 📊 统一响应格式

### 成功响应

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

### 错误响应

```json
{
  "code": 400,
  "message": "错误信息",
  "data": null
}
```

### 状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未授权，需要登录 |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 1. 认证接口

### 1.1 用户登录

**接口地址**: `POST /auth/login`

**请求参数**:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | string | 是 | 用户名 |
| password | string | 是 | 密码 |

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiYWRtaW4iLCJyb2xlIjoiYWRtaW4iLCJpYXQiOjE2OTk1MTIwMDAsImV4cCI6MTcwMDExNjgwMH0.abc123",
    "user": {
      "id": 1,
      "username": "admin",
      "phone": "13800138000",
      "email": null,
      "realName": "系统管理员",
      "avatar": null,
      "role": "admin",
      "balance": 10000.00,
      "totalChargingCount": 0,
      "totalChargingEnergy": 0.00,
      "totalChargingFee": 0.00,
      "vehiclePlate": null,
      "vehicleModel": null,
      "vehicleBrand": null,
      "isVip": false,
      "vipExpireDate": null,
      "status": "active",
      "lastLoginTime": "2024-01-15 10:30:00",
      "createdAt": "2024-01-01 00:00:00"
    }
  }
}
```

### 1.2 用户注册

**接口地址**: `POST /auth/register`

**请求参数**:

```json
{
  "username": "testuser",
  "password": "123456",
  "phone": "13900139000",
  "email": "test@example.com",
  "realName": "测试用户",
  "vehiclePlate": "京A12345",
  "vehicleModel": "Model 3",
  "vehicleBrand": "特斯拉"
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | string | 是 | 用户名(3-50字符) |
| password | string | 是 | 密码(6-20字符) |
| phone | string | 是 | 手机号(11位) |
| email | string | 否 | 邮箱 |
| realName | string | 否 | 真实姓名 |
| vehiclePlate | string | 否 | 车牌号 |
| vehicleModel | string | 否 | 车型 |
| vehicleBrand | string | 否 | 品牌 |

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 2,
    "username": "testuser",
    "phone": "13900139000",
    "role": "user",
    "status": "active"
  }
}
```

---

## 2. 用户管理接口

### 2.1 获取用户列表

**接口地址**: `GET /users`

**权限要求**: ADMIN, OPERATOR

**请求参数**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | integer | 否 | 1 | 页码 |
| size | integer | 否 | 10 | 每页数量 |
| keyword | string | 否 | - | 搜索关键词(用户名/手机号/姓名) |
| role | string | 否 | - | 角色筛选(admin/operator/user) |

**请求示例**:

```http
GET /users?page=1&size=10&keyword=admin&role=admin
```

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "id": 1,
        "username": "admin",
        "phone": "13800138000",
        "email": null,
        "realName": "系统管理员",
        "role": "admin",
        "balance": 10000.00,
        "totalChargingCount": 50,
        "totalChargingEnergy": 1250.50,
        "totalChargingFee": 2500.00,
        "status": "active",
        "createdAt": "2024-01-01 00:00:00"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### 2.2 获取用户详情

**接口地址**: `GET /users/{id}`

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | long | 是 | 用户ID(路径参数) |

**请求示例**:

```http
GET /users/1
```

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "admin",
    "phone": "13800138000",
    "email": "admin@example.com",
    "realName": "系统管理员",
    "avatar": "https://example.com/avatar.jpg",
    "role": "admin",
    "balance": 10000.00,
    "totalChargingCount": 50,
    "totalChargingEnergy": 1250.50,
    "totalChargingFee": 2500.00,
    "vehiclePlate": "京A12345",
    "vehicleModel": "Model 3",
    "vehicleBrand": "特斯拉",
    "isVip": true,
    "vipExpireDate": "2024-12-31",
    "status": "active",
    "lastLoginTime": "2024-01-15 10:30:00",
    "createdAt": "2024-01-01 00:00:00"
  }
}
```

### 2.3 更新用户信息

**接口地址**: `PUT /users/{id}`

**请求参数**:

```json
{
  "phone": "13900139001",
  "email": "newemail@example.com",
  "realName": "新姓名",
  "avatar": "https://example.com/new-avatar.jpg",
  "vehiclePlate": "京B67890",
  "vehicleModel": "Model Y",
  "vehicleBrand": "特斯拉"
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "admin",
    "phone": "13900139001",
    "email": "newemail@example.com"
  }
}
```

### 2.4 删除用户

**接口地址**: `DELETE /users/{id}`

**权限要求**: ADMIN

**请求示例**:

```http
DELETE /users/2
```

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

## 3. 充电桩管理接口

### 3.1 获取充电桩列表

**接口地址**: `GET /stations`

**请求参数**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | integer | 否 | 1 | 页码 |
| size | integer | 否 | 10 | 每页数量 |
| keyword | string | 否 | - | 搜索关键词(名称/编号/位置) |
| type | string | 否 | - | 类型筛选(ac/dc/super) |
| status | string | 否 | - | 状态筛选(online/offline/charging/maintenance/error) |

**请求示例**:

```http
GET /stations?page=1&size=20&type=dc&status=online
```

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "id": 1,
        "name": "充电桩-001",
        "code": "CP-001",
        "type": "dc",
        "status": "online",
        "power": 120.00,
        "location": "北京市朝阳区建国路1号",
        "latitude": 39.9042,
        "longitude": 116.4074,
        "voltage": 380.00,
        "current": 250.00,
        "temperature": 25.50,
        "usageCount": 156,
        "totalEnergy": 3500.80,
        "totalRevenue": 7000.50,
        "lastMaintenanceDate": "2024-01-01",
        "installDate": "2023-06-01",
        "manufacturer": "国家电网",
        "model": "DC-120KW",
        "serialNumber": "SN123456789",
        "createdAt": "2023-06-01 10:00:00",
        "updatedAt": "2024-01-15 14:30:00"
      }
    ],
    "total": 50,
    "size": 20,
    "current": 1,
    "pages": 3
  }
}
```

### 3.2 获取充电桩详情

**接口地址**: `GET /stations/{id}`

**请求示例**:

```http
GET /stations/1
```

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "充电桩-001",
    "code": "CP-001",
    "type": "dc",
    "status": "online",
    "power": 120.00,
    "location": "北京市朝阳区建国路1号",
    "latitude": 39.9042,
    "longitude": 116.4074,
    "voltage": 380.00,
    "current": 250.00,
    "temperature": 25.50,
    "usageCount": 156,
    "totalEnergy": 3500.80,
    "totalRevenue": 7000.50,
    "lastMaintenanceDate": "2024-01-01",
    "installDate": "2023-06-01",
    "manufacturer": "国家电网",
    "model": "DC-120KW",
    "serialNumber": "SN123456789",
    "createdAt": "2023-06-01 10:00:00",
    "updatedAt": "2024-01-15 14:30:00"
  }
}
```

### 3.3 创建充电桩

**接口地址**: `POST /stations`

**权限要求**: ADMIN, OPERATOR

**请求参数**:

```json
{
  "name": "充电桩-100",
  "code": "CP-100",
  "type": "super",
  "power": 180.00,
  "location": "上海市浦东新区世纪大道1号",
  "latitude": 31.2304,
  "longitude": 121.4737,
  "voltage": 750.00,
  "current": 250.00,
  "temperature": 23.00,
  "installDate": "2024-01-15",
  "manufacturer": "特斯拉",
  "model": "Supercharger V3",
  "serialNumber": "SN987654321"
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | string | 是 | 充电桩名称 |
| code | string | 是 | 充电桩编号(唯一) |
| type | string | 是 | 类型(ac/dc/super) |
| power | decimal | 是 | 功率(kW) |
| location | string | 是 | 位置 |
| latitude | decimal | 否 | 纬度 |
| longitude | decimal | 否 | 经度 |
| voltage | decimal | 是 | 电压(V) |
| current | decimal | 是 | 电流(A) |
| temperature | decimal | 否 | 温度(°C) |
| installDate | date | 是 | 安装日期 |
| manufacturer | string | 否 | 制造商 |
| model | string | 否 | 型号 |
| serialNumber | string | 否 | 序列号 |

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 100,
    "name": "充电桩-100",
    "code": "CP-100",
    "type": "super",
    "status": "online"
  }
}
```

### 3.4 更新充电桩

**接口地址**: `PUT /stations/{id}`

**权限要求**: ADMIN, OPERATOR

**请求参数**: 同创建接口

### 3.5 删除充电桩

**接口地址**: `DELETE /stations/{id}`

**权限要求**: ADMIN

### 3.6 更新充电桩状态

**接口地址**: `PATCH /stations/{id}/status`

**权限要求**: ADMIN, OPERATOR

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| status | string | 是 | 状态(online/offline/charging/maintenance/error) |

**请求示例**:

```http
PATCH /stations/1/status?status=maintenance
```

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

## 4. 充电记录接口

### 4.1 获取充电记录列表

**接口地址**: `GET /records`

**请求参数**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | integer | 否 | 1 | 页码 |
| size | integer | 否 | 10 | 每页数量 |
| keyword | string | 否 | - | 搜索关键词(充电桩名称/用户名/手机号) |
| status | string | 否 | - | 状态筛选(charging/completed/cancelled/failed) |
| userId | long | 否 | - | 用户ID筛选 |

**请求示例**:

```http
GET /records?page=1&size=20&status=completed&userId=1
```

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "id": 1,
        "stationId": 1,
        "stationName": "充电桩-001",
        "userId": 1,
        "userName": "admin",
        "userPhone": "13800138000",
        "startTime": "2024-01-15 10:00:00",
        "endTime": "2024-01-15 11:30:00",
        "duration": 90,
        "energy": 45.50,
        "startSoc": 20,
        "endSoc": 80,
        "peakEnergy": 10.00,
        "valleyEnergy": 15.50,
        "normalEnergy": 20.00,
        "electricityFee": 36.40,
        "serviceFee": 36.40,
        "totalFee": 72.80,
        "status": "completed",
        "paymentStatus": "paid",
        "paymentMethod": "wechat",
        "paymentTime": "2024-01-15 11:35:00",
        "createdAt": "2024-01-15 10:00:00"
      }
    ],
    "total": 100,
    "size": 20,
    "current": 1,
    "pages": 5
  }
}
```

### 4.2 获取充电记录详情

**接口地址**: `GET /records/{id}`

**请求示例**:

```http
GET /records/1
```

**响应示例**: 同列表中的单条记录格式

### 4.3 开始充电

**接口地址**: `POST /records/start`

**请求参数**:

```json
{
  "stationId": 1,
  "userId": 1,
  "startTime": "2024-01-15 14:00:00",
  "startSoc": 25
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| stationId | long | 是 | 充电桩ID |
| userId | long | 是 | 用户ID |
| startTime | datetime | 是 | 开始时间 |
| startSoc | integer | 否 | 开始电量(0-100) |

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 101,
    "stationId": 1,
    "stationName": "充电桩-001",
    "userId": 1,
    "userName": "admin",
    "userPhone": "13800138000",
    "startTime": "2024-01-15 14:00:00",
    "startSoc": 25,
    "status": "charging",
    "paymentStatus": "unpaid"
  }
}
```

### 4.4 结束充电

**接口地址**: `POST /records/{id}/end`

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| endTime | datetime | 是 | 结束时间(查询参数) |
| endSoc | integer | 是 | 结束电量(0-100) |

**请求示例**:

```http
POST /records/101/end?endTime=2024-01-15 15:30:00&endSoc=85
```

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 101,
    "startTime": "2024-01-15 14:00:00",
    "endTime": "2024-01-15 15:30:00",
    "duration": 90,
    "energy": 36.00,
    "startSoc": 25,
    "endSoc": 85,
    "electricityFee": 28.80,
    "serviceFee": 28.80,
    "totalFee": 57.60,
    "status": "completed",
    "paymentStatus": "unpaid"
  }
}
```

---

## 5. 仪表盘接口

### 5.1 获取统计数据

**接口地址**: `GET /dashboard/stats`

**请求示例**:

```http
GET /dashboard/stats
```

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "totalStations": 50,
    "onlineStations": 45,
    "chargingStations": 8,
    "offlineStations": 5,
    "todayRecords": 128,
    "todayEnergy": 3500.50,
    "todayRevenue": 7000.80,
    "totalUsers": 500,
    "activeUsers": 320,
    "vipUsers": 50,
    "totalRevenue": 150000.00,
    "totalEnergy": 75000.00,
    "totalRecords": 5000
  }
}
```

---

## 📦 数据模型

### User (用户)

```typescript
{
  id: number;                    // 用户ID
  username: string;              // 用户名
  phone: string;                 // 手机号
  email?: string;                // 邮箱
  realName?: string;             // 真实姓名
  avatar?: string;               // 头像URL
  role: string;                  // 角色(admin/operator/user)
  balance: number;               // 账户余额
  totalChargingCount: number;    // 累计充电次数
  totalChargingEnergy: number;   // 累计充电量(kWh)
  totalChargingFee: number;      // 累计充电费用
  vehiclePlate?: string;         // 车牌号
  vehicleModel?: string;         // 车型
  vehicleBrand?: string;         // 品牌
  isVip: boolean;                // 是否VIP
  vipExpireDate?: string;        // VIP到期日期
  status: string;                // 状态(active/inactive/blocked)
  lastLoginTime?: string;        // 最后登录时间
  createdAt: string;             // 创建时间
}
```

### ChargingStation (充电桩)

```typescript
{
  id: number;                    // 充电桩ID
  name: string;                  // 充电桩名称
  code: string;                  // 充电桩编号
  type: string;                  // 类型(ac/dc/super)
  status: string;                // 状态(online/offline/charging/maintenance/error)
  power: number;                 // 功率(kW)
  location: string;              // 位置
  latitude?: number;             // 纬度
  longitude?: number;            // 经度
  voltage: number;               // 电压(V)
  current: number;               // 电流(A)
  temperature?: number;          // 温度(°C)
  usageCount: number;            // 使用次数
  totalEnergy: number;           // 总充电量(kWh)
  totalRevenue: number;          // 总收入(元)
  lastMaintenanceDate?: string;  // 最后维护日期
  installDate: string;           // 安装日期
  manufacturer?: string;         // 制造商
  model?: string;                // 型号
  serialNumber?: string;         // 序列号
  createdAt: string;             // 创建时间
  updatedAt: string;             // 更新时间
}
```

### ChargingRecord (充电记录)

```typescript
{
  id: number;                    // 记录ID
  stationId: number;             // 充电桩ID
  stationName: string;           // 充电桩名称
  userId: number;                // 用户ID
  userName: string;              // 用户名
  userPhone: string;             // 用户手机号
  startTime: string;             // 开始时间
  endTime?: string;              // 结束时间
  duration?: number;             // 时长(分钟)
  energy?: number;               // 充电量(kWh)
  startSoc?: number;             // 开始电量(%)
  endSoc?: number;               // 结束电量(%)
  peakEnergy: number;            // 峰时电量(kWh)
  valleyEnergy: number;          // 谷时电量(kWh)
  normalEnergy: number;          // 平时电量(kWh)
  electricityFee: number;        // 电费(元)
  serviceFee: number;            // 服务费(元)
  totalFee: number;              // 总费用(元)
  status: string;                // 状态(charging/completed/cancelled/failed)
  paymentStatus: string;         // 支付状态(unpaid/paid/refunded)
  paymentMethod?: string;        // 支付方式(wechat/alipay/card)
  paymentTime?: string;          // 支付时间
  createdAt: string;             // 创建时间
}
```

---

## 🔒 权限说明

### 角色类型

| 角色 | 说明 | 权限 |
|------|------|------|
| **admin** | 管理员 | 所有权限 |
| **operator** | 运营人员 | 充电桩管理、用户查看 |
| **user** | 普通用户 | 查看自己的信息和充电记录 |

### 接口权限矩阵

| 接口分类 | admin | operator | user |
|---------|-------|----------|------|
| 登录/注册 | ✅ | ✅ | ✅ |
| 用户列表 | ✅ | ✅ | ❌ |
| 用户详情 | ✅ | ✅ | ✅(仅自己) |
| 更新用户 | ✅ | ❌ | ✅(仅自己) |
| 删除用户 | ✅ | ❌ | ❌ |
| 充电桩列表 | ✅ | ✅ | ✅ |
| 充电桩详情 | ✅ | ✅ | ✅ |
| 创建充电桩 | ✅ | ✅ | ❌ |
| 更新充电桩 | ✅ | ✅ | ❌ |
| 删除充电桩 | ✅ | ❌ | ❌ |
| 充电记录 | ✅ | ✅ | ✅(仅自己) |
| 开始/结束充电 | ✅ | ✅ | ✅ |
| 仪表盘统计 | ✅ | ✅ | ✅ |

---

## 📝 错误码说明

### 业务错误码

| 错误码 | 说明 |
|--------|------|
| 1001 | 用户不存在 |
| 1002 | 密码错误 |
| 1003 | 账号已被禁用 |
| 1004 | 用户名已存在 |
| 1005 | 手机号已被注册 |
| 2001 | 充电桩不存在 |
| 2002 | 充电桩不可用 |
| 2003 | 充电桩编号已存在 |
| 3001 | 充电记录不存在 |
| 3002 | 充电已结束 |
| 3003 | 费率配置不存在 |

---

## 🧪 测试示例

### 使用cURL

```bash
# 1. 登录获取Token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 2. 使用Token访问接口
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."

# 3. 创建充电桩
curl -X POST http://localhost:8080/api/stations \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "name": "充电桩-100",
    "code": "CP-100",
    "type": "dc",
    "power": 120.00,
    "location": "北京市朝阳区",
    "voltage": 380.00,
    "current": 250.00,
    "installDate": "2024-01-15"
  }'
```

### 使用Postman

1. 导入集合: 访问 http://localhost:8080/api/swagger-ui.html
2. 设置环境变量: `base_url` = `http://localhost:8080/api`
3. 设置认证: Authorization → Bearer Token → {{token}}

### 使用JavaScript (Axios)

```javascript
import axios from 'axios';

// 配置axios
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 1. 登录
const login = async () => {
  const response = await api.post('/auth/login', {
    username: 'admin',
    password: 'admin123'
  });
  localStorage.setItem('token', response.data.data.token);
  return response.data;
};

// 2. 获取充电桩列表
const getStations = async () => {
  const response = await api.get('/stations', {
    params: { page: 1, size: 10 }
  });
  return response.data;
};

// 3. 开始充电
const startCharging = async (stationId, userId) => {
  const response = await api.post('/records/start', {
    stationId,
    userId,
    startTime: new Date().toISOString(),
    startSoc: 20
  });
  return response.data;
};
```

---

## 📚 相关文档

- **Swagger在线文档**: http://localhost:8080/api/swagger-ui.html
- **后端源码**: [backend/](../backend/)
- **前端API封装**: [src/api/](../src/api/)
- **部署指南**: [DEPLOY.md](../DEPLOY.md)

---

## 📮 联系支持

如有API使用问题，请联系技术支持。

**最后更新**: 2024-01-15  
**API版本**: v1.0
