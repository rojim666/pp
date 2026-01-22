# 充电桩管理系统后端

基于 Spring Boot 3.1.5 + MyBatis-Plus 的充电桩管理系统后端服务。

## 技术栈

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Security** - 安全认证
- **MyBatis-Plus 3.5.4.1** - ORM框架
- **MySQL 8.0** - 数据库
- **Redis** - 缓存
- **JWT 0.11.5** - Token认证
- **SpringDoc OpenAPI 2.2.0** - API文档
- **Hutool 5.8.22** - 工具库
- **Lombok** - 简化代码

## 项目结构

```
backend/
├── src/main/java/com/charging/
│   ├── ChargingStationApplication.java    # 启动类
│   ├── common/                            # 通用模块
│   │   ├── exception/                     # 异常处理
│   │   └── result/                        # 统一响应
│   ├── config/                            # 配置类
│   │   ├── SecurityConfig.java            # Security配置
│   │   └── MyBatisPlusMetaObjectHandler.java
│   ├── controller/                        # 控制器
│   │   ├── AuthController.java            # 认证接口
│   │   ├── UserController.java            # 用户接口
│   │   ├── ChargingStationController.java # 充电桩接口
│   │   ├── ChargingRecordController.java  # 充电记录接口
│   │   └── DashboardController.java       # 仪表盘接口
│   ├── dto/                               # 数据传输对象
│   │   ├── request/                       # 请求DTO
│   │   └── response/                      # 响应DTO
│   ├── entity/                            # 实体类
│   │   ├── User.java
│   │   ├── ChargingStation.java
│   │   ├── ChargingRecord.java
│   │   ├── TariffConfig.java
│   │   └── SystemLog.java
│   ├── mapper/                            # MyBatis Mapper
│   ├── security/                          # 安全模块
│   │   └── JwtAuthenticationFilter.java
│   ├── service/                           # 业务服务
│   └── util/                              # 工具类
│       └── JwtUtil.java
├── src/main/resources/
│   └── application.yml                    # 配置文件
├── sql/
│   └── schema.sql                         # 数据库脚本
└── pom.xml                                # Maven配置
```

## 快速开始

### 1. 环境准备

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 2. 数据库初始化

```bash
# 登录MySQL
mysql -u root -p

# 执行SQL脚本
source sql/schema.sql
```

### 3. 配置文件

修改 `application.yml` 中的数据库和Redis配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/charging_station
    username: root
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
```

### 4. 运行项目

```bash
# 进入backend目录
cd backend

# Maven编译打包
mvn clean package

# 运行
java -jar target/charging-station-backend.jar
```

或使用Maven直接运行：

```bash
mvn spring-boot:run
```

### 5. 访问API文档

启动成功后访问：
- API文档: http://localhost:8080/api/swagger-ui.html
- 接口: http://localhost:8080/api/v3/api-docs

## API接口

### 认证接口 `/auth`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /auth/login | 用户登录 |
| POST | /auth/register | 用户注册 |

### 用户接口 `/users`

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | /users | 获取用户列表 | ADMIN/OPERATOR |
| GET | /users/{id} | 获取用户详情 | ALL |
| PUT | /users/{id} | 更新用户信息 | ALL |
| DELETE | /users/{id} | 删除用户 | ADMIN |

### 充电桩接口 `/stations`

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | /stations | 获取充电桩列表 | ALL |
| GET | /stations/{id} | 获取充电桩详情 | ALL |
| POST | /stations | 创建充电桩 | ADMIN/OPERATOR |
| PUT | /stations/{id} | 更新充电桩 | ADMIN/OPERATOR |
| DELETE | /stations/{id} | 删除充电桩 | ADMIN |
| PATCH | /stations/{id}/status | 更新状态 | ADMIN/OPERATOR |

### 充电记录接口 `/records`

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | /records | 获取充电记录列表 | ALL |
| GET | /records/{id} | 获取充电记录详情 | ALL |
| POST | /records/start | 开始充电 | ALL |
| POST | /records/{id}/end | 结束充电 | ALL |

### 仪表盘接口 `/dashboard`

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | /dashboard/stats | 获取统计数据 | ALL |

## 默认账户

```
用户名: admin
密码: admin123
```

## 认证方式

使用JWT Token认证，请求头格式：

```
Authorization: Bearer {token}
```

登录成功后获取token，后续请求携带此token即可。

## 数据库表

- `sys_user` - 用户表
- `charging_station` - 充电桩表
- `charging_record` - 充电记录表
- `tariff_config` - 费率配置表
- `system_log` - 系统日志表

## 开发注意事项

### 1. 统一响应格式

所有接口返回统一的Result格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

### 2. 异常处理

- `BusinessException` - 业务异常
- `GlobalExceptionHandler` - 全局异常处理器

### 3. 参数校验

使用JSR-303验证注解：
- `@NotBlank` - 非空
- `@Email` - 邮箱格式
- `@Pattern` - 正则匹配
- `@Min/@Max` - 数值范围

### 4. 分页查询

使用MyBatis-Plus的Page对象：

```java
Page<Entity> page = new Page<>(pageNum, pageSize);
IPage<Entity> result = mapper.selectPage(page, wrapper);
```

### 5. 日志

使用Lombok的`@Slf4j`注解：

```java
log.info("日志信息");
log.error("错误信息", exception);
```

## 性能优化建议

1. **Redis缓存**: 缓存热点数据(充电桩列表、用户信息等)
2. **数据库索引**: 已为常用查询字段创建索引
3. **连接池**: 使用HikariCP连接池
4. **异步处理**: 系统日志可异步记录
5. **分页查询**: 大数据量使用分页

## License

MIT License
