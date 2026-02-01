@echo off
chcp 65001 > nul
echo.
echo ╔════════════════════════════════════════════════════════╗
echo ║       充电桩管理系统 - 后端启动                       ║
echo ╚════════════════════════════════════════════════════════╝
echo.

REM 检查MySQL服务
echo 🔍 正在检查MySQL服务...
net start | find "MySQL" >nul
if %errorlevel% equ 0 (
    echo ✅ MySQL服务已启动
) else (
    echo ⚠️  MySQL服务未启动，正在启动...
    net start MySQL95 >nul 2>&1
    if %errorlevel% equ 0 (
        echo ✅ MySQL服务已启动
    ) else (
        echo ❌ 无法启动MySQL服务
    )
)

echo.
echo 🚀 正在启动后端服务...
echo.
echo 📍 访问地址:
echo    - API文档: http://localhost:8080/api/swagger-ui.html
echo    - 健康检查: http://localhost:8080/api/actuator/health
echo.
echo 🔌 MQTT配置:
echo    - 服务器: 129.204.21.66:1883
echo    - 用户名: charge
echo    - 订阅话题: /device/+/+
echo.

java -jar target\charging-station-backend-1.0.0.jar

pause
