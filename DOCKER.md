# Docker 部署快速指南

## 前置要求

- Docker 已安装（版本 20.10+）
- Docker Compose 已安装（版本 1.29+）
- 至少 4GB RAM，20GB 存储空间

## 快速开始

### 1. 准备环境文件

```bash
# 复制环境配置模板
cp .env.example .env

# 编辑 .env 文件，设置你的配置
# 重要：修改以下项
# - DB_PASSWORD: 数据库密码
# - JWT_SECRET: JWT 密钥
# - ALLOWED_ORIGINS: 允许的域名
```

### 2. 构建镜像

```bash
# 构建前端和后端镜像
docker-compose build

# 或单独构建
docker-compose build backend
docker-compose build frontend
```

### 3. 启动服务

```bash
# 启动所有服务（后台运行）
docker compose up -d

# 查看启动日志
docker compose logs -f

# 等待所有服务健康（约 30-60 秒）
```

### 4. 验证部署

```bash
# 检查所有容器状态
docker-compose ps

# 检查特定服务日志
docker-compose logs backend
docker-compose logs frontend
docker-compose logs mysql

# 测试前端
curl http://localhost

# 测试后端 API
curl http://localhost:8080/api/health

# 测试数据库连接
docker exec charging-mysql mysql -ucharging -pcharging123 -e "SELECT 1;"
```

## 常见操作

### 查看日志

```bash
# 实时查看所有服务日志
docker-compose logs -f

# 查看指定服务日志
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mysql

# 查看最后 100 行日志
docker-compose logs --tail=100 backend
```

### 停止和启动

```bash
# 停止所有服务
docker-compose stop

# 启动已停止的服务
docker-compose start

# 重启服务
docker-compose restart backend

# 完全移除容器（保留数据）
docker-compose down

# 移除所有内容，包括数据
docker-compose down -v
```

### 更新应用

```bash
# 1. 更新代码
git pull

# 2. 重新构建镜像
docker-compose build --no-cache

# 3. 重启服务
docker-compose up -d

# 4. 查看日志确认启动成功
docker-compose logs -f
```

### 查看容器内部

```bash
# 进入后端容器
docker-compose exec backend sh

# 进入前端容器
docker-compose exec frontend sh

# 进入数据库容器
docker-compose exec mysql mysql -ucharging -pcharging123 charging_station
```

### 数据库管理

```bash
# 导入数据库备份
docker-compose exec -T mysql mysql -ucharging -pcharging123 charging_station < backup.sql

# 导出数据库备份
docker-compose exec mysql mysqldump -ucharging -pcharging123 charging_station > backup.sql

# 创建新的数据库用户
docker-compose exec mysql mysql -uroot -proot -e "CREATE USER 'newuser'@'%' IDENTIFIED BY 'password';"
```

## 持久化数据

数据库数据自动保存在 Docker 卷中：

```bash
# 查看所有数据卷
docker volume ls | grep charging

# 查看卷详情
docker volume inspect charging_mysql-data

# 手动备份数据
docker run --rm -v charging_mysql-data:/data -v $(pwd):/backup \
  alpine tar czf /backup/mysql-backup.tar.gz -C /data .

# 手动恢复数据
docker run --rm -v charging_mysql-data:/data -v $(pwd):/backup \
  alpine tar xzf /backup/mysql-backup.tar.gz -C /data
```

## 扩展和负载均衡

### 运行多个后端实例

```yaml
# 修改 docker-compose.yml
backend1:
  image: charging-backend:latest
  ports:
    - "8081:8080"
  
backend2:
  image: charging-backend:latest
  ports:
    - "8082:8080"

# Nginx 会自动负载均衡到所有健康实例
```

### 查看 Nginx 负载均衡状态

```bash
docker-compose exec frontend nginx -T
```

## 故障排除

### 问题 1: 容器无法启动

```bash
# 查看详细错误日志
docker-compose logs backend

# 检查容器健康状态
docker-compose ps

# 重新构建镜像
docker-compose build --no-cache backend
```

### 问题 2: 数据库连接失败

```bash
# 检查 MySQL 状态
docker-compose logs mysql

# 测试连接
docker-compose exec mysql mysql -ucharging -pcharging123 -e "SELECT 1;"

# 检查网络连接
docker network inspect charging_charging-network
```

### 问题 3: API 无法访问

```bash
# 检查后端日志
docker-compose logs backend

# 测试内部连接
docker-compose exec frontend curl http://backend:8080/api/health

# 检查端口映射
docker-compose ps | grep backend
```

### 问题 4: 磁盘空间不足

```bash
# 检查 Docker 磁盘使用
docker system df

# 清理未使用的镜像
docker image prune -a

# 清理未使用的卷
docker volume prune
```

## 性能调优

### 增加内存限制

编辑 `docker-compose.yml`：

```yaml
services:
  backend:
    deploy:
      resources:
        limits:
          memory: 2G
        reservations:
          memory: 1G
```

### 增加 MySQL 连接数

编辑 `docker-compose.yml` 中的 MySQL 命令：

```yaml
mysql:
  command:
    - --max_connections=1000
```

### 启用 Nginx 缓存

编辑 `nginx.conf`：

```nginx
proxy_cache_path /var/cache/nginx levels=1:2 keys_zone=my_cache:10m;

location /api/ {
    proxy_cache my_cache;
    proxy_cache_valid 200 10m;
}
```

## 升级到生产环境

### 配置 HTTPS

1. 获取 SSL 证书：

```bash
# 使用 Let's Encrypt
docker run -it --rm -v $(pwd)/certs:/etc/letsencrypt \
  certbot/certbot certonly --standalone -d your-domain.com
```

2. 修改 `nginx-default.conf`：

```nginx
server {
    listen 443 ssl http2;
    ssl_certificate /etc/nginx/certs/fullchain.pem;
    ssl_certificate_key /etc/nginx/certs/privkey.pem;
}
```

3. 重启 Nginx：

```bash
docker-compose restart frontend
```

### 配置监控和日志

使用 ELK Stack：

```bash
# 启动 Elasticsearch
docker run -d --name elasticsearch -e discovery.type=single-node \
  docker.elastic.co/elasticsearch/elasticsearch:8.0.0

# 启动 Kibana
docker run -d --name kibana -e ELASTICSEARCH_HOSTS=http://elasticsearch:9200 \
  docker.elastic.co/kibana/kibana:8.0.0
```

## 备份和恢复

### 完整备份

```bash
#!/bin/bash
BACKUP_DIR="./backups"
mkdir -p $BACKUP_DIR

# 备份数据库
docker-compose exec -T mysql mysqldump -ucharging -pcharging123 charging_station \
  | gzip > $BACKUP_DIR/db-$(date +%Y%m%d-%H%M%S).sql.gz

# 备份配置
tar czf $BACKUP_DIR/config-$(date +%Y%m%d-%H%M%S).tar.gz .env

echo "备份完成"
```

### 完整恢复

```bash
# 恢复数据库
docker-compose exec -T mysql mysql -ucharging -pcharging123 charging_station \
  < backups/db-20231101-120000.sql

echo "恢复完成"
```

## 安全最佳实践

1. **修改默认密码**
   ```bash
   编辑 .env，修改所有密码和密钥
   ```

2. **启用防火墙**
   ```bash
   ufw allow 80/tcp
   ufw allow 443/tcp
   ```

3. **定期更新**
   ```bash
   docker-compose pull
   docker-compose up -d
   ```

4. **限制容器资源**
   ```yaml
   deploy:
     resources:
       limits:
         cpus: '1'
         memory: 1G
   ```

## 性能基准

在标准配置下的预期性能：

- **并发用户**: 100+
- **请求/秒**: 500+
- **平均响应时间**: < 200ms
- **数据库查询**: < 100ms

## 支持

更多信息请参考：
- [DEPLOYMENT.md](./DEPLOYMENT.md) - 详细部署指南
- [Docker 官方文档](https://docs.docker.com/)
- [Docker Compose 文档](https://docs.docker.com/compose/)
