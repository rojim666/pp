@echo off
chcp 65001 > nul
setlocal enabledelayedexpansion

echo.
echo ╔════════════════════════════════════════════════════════╗
echo ║       充电桩管理系统 - 后端构建脚本                   ║
echo ╚════════════════════════════════════════════════════════╝
echo.

REM 检查Java是否安装
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 错误: 未找到Java，请先安装JDK 17或更高版本
    pause
    exit /b 1
)

REM 检查是否存在Maven wrapper
if exist "mvnw.cmd" (
    echo ✅ 使用Maven Wrapper进行构建
    call mvnw.cmd clean package -DskipTests
) else (
    echo ❌ 错误: 未找到Maven Wrapper，请确保在项目根目录执行此脚本
    pause
    exit /b 1
)

if %errorlevel% equ 0 (
    echo.
    echo ✅ 构建成功！
    echo.
    echo 📝 后续步骤:
    echo   1. 运行: java -jar target\charging-station-backend-1.0.0.jar
    echo   2. 访问: http://localhost:8080/api/swagger-ui.html
    echo.
) else (
    echo.
    echo ❌ 构建失败！
    echo.
)

pause
