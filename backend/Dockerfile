# 后端构建和部署 Dockerfile
# 阶段 1: 构建
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# 复制 pom.xml
COPY backend/pom.xml .

# 下载依赖（分层缓存）
RUN mvn dependency:go-offline -B -U \
    -Dmaven.repo.local=/root/.m2/repository \
    -Dmaven.wagon.http.ssl.insecure=true \
    -Dmaven.wagon.http.ssl.allowall=true \
    -Dmaven.mirror.url=https://maven.aliyun.com/repository/public

# 复制源代码
COPY backend/src ./src

# 构建应用（跳过测试以加快速度）
RUN mvn clean package -DskipTests

# 阶段 2: 运行
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# 安装 curl（用于健康检查）
RUN apk add --no-cache curl

# 从构建阶段复制 JAR 文件
COPY --from=builder /app/target/charging-station-backend-1.0.0.jar app.jar

# 创建日志目录
RUN mkdir -p logs && chmod -R 755 logs

# 暴露端口
EXPOSE 8080

# 设置时区
ENV TZ=Asia/Shanghai

# JVM 调优参数
ENV JAVA_OPTS="-Xmx1G -Xms512M -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Dfile.encoding=UTF-8"

# 启动应用
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=10s --retries=3 \
    CMD curl -f http://localhost:8080/api/health || exit 1
