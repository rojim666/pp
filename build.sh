#!/bin/bash

# 充电桩管理系统 - 本地构建脚本
# 此脚本用于在本地构建前端和后端

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的信息
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 获取脚本所在目录
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

print_info "开始构建充电桩管理系统..."
print_info "工作目录: $(pwd)"

# ===== 构建前端 =====
print_info "====== 构建前端应用 ======"
if [ -d "node_modules" ]; then
    print_info "依赖已安装，跳过 npm install"
else
    print_info "安装前端依赖..."
    npm install
    if [ $? -ne 0 ]; then
        print_error "前端依赖安装失败"
        exit 1
    fi
fi

print_info "生成前端生产构建..."
npm run build
if [ $? -ne 0 ]; then
    print_error "前端构建失败"
    exit 1
fi
print_success "前端构建完成"

# ===== 构建后端 =====
print_info "====== 构建后端应用 ======"
cd backend

if ! command -v mvn &> /dev/null; then
    print_error "Maven 未安装，请先安装 Maven"
    exit 1
fi

print_info "使用 Maven 构建后端..."
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    print_error "后端构建失败"
    exit 1
fi

# 检查 JAR 文件是否生成
if [ ! -f "target/charging-station-backend-1.0.0.jar" ]; then
    print_error "JAR 文件未生成"
    exit 1
fi

print_success "后端构建完成"
print_success "JAR 文件位置: $(pwd)/target/charging-station-backend-1.0.0.jar"

cd ..

print_info "====== 构建完成 ======"
print_success "前端构建输出: ./dist"
print_success "后端构建输出: ./backend/target/charging-station-backend-1.0.0.jar"

print_info ""
print_info "下一步:"
print_info "1. Docker 部署: docker-compose up -d"
print_info "2. 本地部署: 参考 DEPLOYMENT.md"
