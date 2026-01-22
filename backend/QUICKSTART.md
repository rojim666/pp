# 充电桩管理系统 - 后端快速启动指南

## 🎯 快速启动(3步)

### 步骤1: 准备数据库

```powershell
# 启动MySQL
net start MySQL80

# 创建数据库(首次运行)
mysql -u root -p < backend/sql/schema.sql
```

### 步骤2: 修改配置(如需要)

编辑 `backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    username: root       # 修改为你的MySQL用户名
    password: root       # 修改为你的MySQL密码
  data:
    redis:
      host: localhost    # Redis地址
```

### 步骤3: 启动后端

```powershell
cd backend
mvn spring-boot:run
```

或使用启动脚本:
```powershell
cd backend
.\start.bat
```

## 📊 验证启动成功

### 1. 查看控制台输出

看到以下内容表示启动成功:
```
   ______                      _                ____  __        __  _           
  / ____/___ ___  _____ ___  (_)___  ____ _   / __ \/ /_  __  / /_(_)___  ____ 
 / /   / __ `__ \/ __ `/ _ \/ / __ \/ __ `/  / /_/ / / / / / / __/ / __ \/ __ \
/ /___/ / / / / / /_/ /  __/ / / / / /_/ /  / _, _/ / /_/ / / /_/ / /_/ / / / /
\____/_/ /_/ /_/\__,_/\___/_/_/ /_/\__, /  /_/ |_/_/\__,_/  \__/_/\____/_/ /_/ 
                                   /____/                                       

充电桩管理系统启动成功！
访问地址: http://localhost:8080/api
API文档: http://localhost:8080/api/swagger-ui.html
```

### 2. 测试API

打开浏览器访问:
- **API文档**: http://localhost:8080/api/swagger-ui.html
- **健康检查**: http://localhost:8080/api/actuator/health

### 3. 测试登录

使用Postman或curl测试:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

成功返回:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "user": {
      "id": 1,
      "username": "admin",
      "role": "admin"
    }
  }
}
```

## 🔧 常见问题

### Q1: 找不到数据库

**错误**: `Unknown database 'charging_station'`

**解决**:
```bash
mysql -u root -p < backend/sql/schema.sql
```

### Q2: Redis连接失败

**错误**: `Unable to connect to Redis`

**解决**:
```bash
# 启动Redis(Windows)
redis-server

# 或者临时注释Redis配置
# application.yml中注释掉spring.data.redis部分
```

### Q3: 端口8080被占用

**错误**: `Port 8080 is already in use`

**解决**:
```powershell
# 查找占用进程
netstat -ano | findstr :8080

# 杀死进程
taskkill /F /PID <进程ID>
```

### Q4: Maven打包失败

**错误**: `Failed to execute goal`

**解决**:
```bash
# 清理并重新打包
mvn clean install -DskipTests

# 更新依赖
mvn dependency:resolve
```

## 📡 API接口列表

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册

### 用户接口
- `GET /api/users` - 用户列表
- `GET /api/users/{id}` - 用户详情
- `PUT /api/users/{id}` - 更新用户
- `DELETE /api/users/{id}` - 删除用户

### 充电桩接口
- `GET /api/stations` - 充电桩列表
- `GET /api/stations/{id}` - 充电桩详情
- `POST /api/stations` - 创建充电桩
- `PUT /api/stations/{id}` - 更新充电桩
- `DELETE /api/stations/{id}` - 删除充电桩
- `PATCH /api/stations/{id}/status` - 更新状态

### 充电记录接口
- `GET /api/records` - 充电记录列表
- `GET /api/records/{id}` - 充电记录详情
- `POST /api/records/start` - 开始充电
- `POST /api/records/{id}/end` - 结束充电

### 仪表盘接口
- `GET /api/dashboard/stats` - 统计数据

## 🔐 默认账户

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | ADMIN |

## 📝 下一步

后端启动成功后，前端会自动连接到后端API。

确保：
1. ✅ 后端运行在 http://localhost:8080
2. ✅ 前端运行在 http://localhost:3000
3. ✅ 前端`.env`配置正确: `VITE_API_BASE_URL=http://localhost:8080/api`

然后就可以在前端页面测试完整功能了！
