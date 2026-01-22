# Docker 端口冲突解决方案

## 问题分析
端口 3306 已被使用，可能是：
1. 之前的 Docker 容器还在运行
2. 系统上已有 MySQL 服务在运行
3. 其他应用占用了该端口

---

## 🔧 解决方案

### 方案 1: 停止并删除旧容器（推荐）

```bash
# 1. 查看所有运行中的 Docker 容器
docker ps

# 2. 查看所有容器（包括已停止的）
docker ps -a

# 3. 停止 MySQL 容器
docker stop charging-mysql

# 4. 删除 MySQL 容器
docker rm charging-mysql

# 5. 查看 docker-compose 正在运行的服务
docker-compose ps

# 6. 停止所有服务
docker-compose down

# 7. 完全清理（包括删除卷）
docker-compose down -v

# 8. 重新启动
docker-compose up -d
```

### 方案 2: 修改端口映射

如果你想保持现有容器运行，修改 `docker-compose.yml`：

```yaml
services:
  mysql:
    ports:
      - "3307:3306"  # 改为 3307，避免冲突
```

然后：
```bash
# 更新容器
docker-compose up -d

# 重新启动
docker-compose restart mysql
```

### 方案 3: 检查端口占用情况

**在 Linux/Mac 上**：
```bash
# 查看哪个进程占用了 3306 端口
lsof -i :3306

# 或者
netstat -tulpn | grep 3306

# 杀死占用 3306 的进程（需要知道 PID）
kill -9 <PID>
```

**在 Windows 上**：
```bash
# 查看占用 3306 的进程
netstat -ano | findstr :3306

# 杀死该进程（PID 可从上一条命令获取）
taskkill /PID <PID> /F
```

### 方案 4: 使用不同的端口

修改 `docker-compose.yml`：

```yaml
services:
  mysql:
    container_name: charging-mysql
    image: mysql:8.0
    ports:
      - "3307:3306"  # 宿主机端口改为 3307
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD:-123456}
      MYSQL_DATABASE: charging_station
      MYSQL_CHARSET: utf8mb4
    volumes:
      - mysql_data:/var/lib/mysql
      - ./backend/sql/schema-utf8.sql:/docker-entrypoint-initdb.d/schema.sql
```

然后更新应用配置：

**application.yml**:
```yaml
spring:
  datasource:
    url: jdbc:mysql://mysql:3307/charging_station  # 改为 3307
    username: root
    password: ${DB_PASSWORD:123456}
```

---

## 🚀 完整修复流程

```bash
# Step 1: 进入项目目录
cd /path/to/charging-station-management

# Step 2: 停止所有服务
docker-compose down -v

# Step 3: 删除所有相关容器（如果需要）
docker container prune -f

# Step 4: 检查端口是否释放
# Linux/Mac:
lsof -i :3306

# Windows (PowerShell):
netstat -ano | findstr :3306

# Step 5: 如果仍被占用，杀死进程
# Linux/Mac:
sudo kill -9 <PID>

# Windows (PowerShell as Admin):
taskkill /PID <PID> /F

# Step 6: 重新启动所有服务
docker-compose up -d

# Step 7: 验证所有容器是否运行
docker-compose ps

# Step 8: 检查日志
docker-compose logs -f
```

---

## 📋 预防措施

### 在启动之前先检查

```bash
# 检查端口是否可用
docker port mysql 3306 2>/dev/null || echo "Port is free"

# 或者：
nc -z 127.0.0.1 3306 && echo "Port in use" || echo "Port is free"
```

### 使用更安全的 docker-compose 配置

```yaml
version: '3.8'

services:
  mysql:
    container_name: charging-mysql
    image: mysql:8.0
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD:-123456}
      MYSQL_DATABASE: charging_station
    volumes:
      - mysql_data:/var/lib/mysql
    restart: unless-stopped
    networks:
      - charging-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  backend:
    depends_on:
      mysql:
        condition: service_healthy
    networks:
      - charging-network

networks:
  charging-network:
    driver: bridge

volumes:
  mysql_data:
```

---

## ✅ 验证修复

```bash
# 1. 检查容器是否运行
docker-compose ps

# 输出应该类似于：
# NAME                COMMAND                  SERVICE             STATUS              PORTS
# charging-mysql      "docker-entrypoint.s…"   mysql               Up 2 minutes        0.0.0.0:3306->3306/tcp

# 2. 测试数据库连接
docker exec charging-mysql mysql -uroot -p123456 -e "SELECT 1"

# 3. 查看数据库
docker exec charging-mysql mysql -uroot -p123456 charging_station -e "SHOW TABLES;"

# 4. 检查后端能否连接到数据库
curl http://localhost:8080/api/health
```

---

## 🆘 如果还是不行

### 完全重置 Docker

```bash
# 非常谨慎：完全删除所有容器、镜像和卷
docker system prune -a --volumes

# 重新构建镜像
docker-compose build --no-cache

# 重新启动
docker-compose up -d
```

### 使用替代端口

```bash
# 修改 docker-compose.yml 使用不同的端口
sed -i 's/3306:3306/3307:3306/g' docker-compose.yml

# 同时更新应用配置
sed -i 's/:3306/\/3307/g' backend/src/main/resources/application.yml

# 重启
docker-compose restart
```

---

## 📞 快速参考

| 命令 | 作用 |
|------|------|
| `docker-compose ps` | 查看运行中的容器 |
| `docker-compose logs` | 查看日志 |
| `docker-compose down` | 停止所有服务 |
| `docker-compose down -v` | 停止并删除卷 |
| `docker-compose up -d` | 启动所有服务 |
| `docker-compose restart mysql` | 重启 MySQL |

---

**推荐步骤**:
1. 运行 `docker-compose down -v` 完全停止
2. 确认端口释放 (`netstat -ano | findstr :3306`)
3. 运行 `docker-compose up -d` 重新启动
4. 验证 `docker-compose ps`

这样通常能解决问题！
