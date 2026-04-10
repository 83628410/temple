#!/bin/bash

# Temple Docker 快速启动脚本

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# 检查Docker是否安装
check_docker() {
    if ! command -v docker &> /dev/null; then
        print_error "Docker 未安装，请先安装 Docker"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        print_error "Docker Compose 未安装，请先安装 Docker Compose"
        exit 1
    fi
    
    print_success "Docker 环境检查通过"
}

# 显示帮助信息
show_help() {
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  backend    仅启动后端服务（MySQL + Redis + Backend）"
    echo "  frontend   启动完整服务（Frontend + Backend + MySQL + Redis）"
    echo "  stop       停止所有服务"
    echo "  clean      清理所有容器和数据卷"
    echo "  logs       查看服务日志"
    echo "  status     查看服务状态"
    echo "  help       显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 backend     # 启动后端服务"
    echo "  $0 frontend    # 启动完整服务"
    echo "  $0 stop        # 停止所有服务"
}

# 清理旧服务
cleanup_old_services() {
    print_info "清理旧的容器、卷、镜像和日志..."
    
    # 清理后端旧服务
    if [ -d "docker/backend" ]; then
        cd docker/backend
        print_info "清理后端旧容器和卷..."
        docker-compose down -v --remove-orphans 2>/dev/null || true
        cd ../..
        
        # 清理后端日志文件
        if [ -d "docker/backend/logs" ]; then
            print_info "清理后端日志文件..."
            rm -rf docker/backend/logs/*.log 2>/dev/null || true
        fi
    fi
    
    # 清理前端旧服务
    if [ -d "docker/frontend" ]; then
        cd docker/frontend
        print_info "清理前端旧容器和卷..."
        docker-compose down -v --remove-orphans 2>/dev/null || true
        cd ../..
        
        # 清理前端日志文件
        if [ -d "docker/frontend/logs" ]; then
            print_info "清理前端日志文件..."
            rm -rf docker/frontend/logs/*.log 2>/dev/null || true
        fi
    fi
    
    # 清理根目录日志文件（如果存在）
    if [ -d "logs" ]; then
        print_info "清理根目录日志文件..."
        rm -rf logs/*.log 2>/dev/null || true
    fi
    
    # 清理悬空的镜像（dangling images）
    print_info "清理悬空的Docker镜像..."
    docker image prune -f >/dev/null 2>&1 || true
    
    print_success "旧服务清理完成"
}

# 启动后端服务
start_backend() {
    # 先清理旧服务
    cleanup_old_services
    
    print_info "启动后端服务..."
    cd docker/backend
    
    print_info "构建并启动 MySQL、Redis 和 Backend..."
    docker-compose up -d --build
    
    print_success "后端服务启动成功！"
    echo ""
    print_info "访问地址："
    echo "  - 后端 API: http://localhost:8080/system"
    echo "  - API 文档: http://localhost:8080/system/doc.html"
    echo ""
    print_info "查看日志: docker-compose logs -f"
}

# 启动前端服务
start_frontend() {
    # 先清理旧服务
    cleanup_old_services
    
    print_info "启动完整服务（前端 + 后端）..."
    cd docker/frontend
    
    print_info "构建并启动所有服务..."
    docker-compose up -d --build
    
    print_success "所有服务启动成功！"
    echo ""
    print_info "访问地址："
    echo "  - 前端应用: http://localhost:80"
    echo "  - 后端 API: http://localhost:8080/system"
    echo "  - API 文档: http://localhost:8080/system/doc.html"
    echo ""
    print_info "查看日志: docker-compose logs -f"
}

# 停止服务
stop_services() {
    print_info "停止所有服务..."
    
    if [ -d "docker/backend" ]; then
        cd docker/backend
        docker-compose stop 2>/dev/null || true
        cd ../..
    fi
    
    if [ -d "docker/frontend" ]; then
        cd docker/frontend
        docker-compose stop 2>/dev/null || true
        cd ../..
    fi
    
    print_success "所有服务已停止"
}

# 清理服务
clean_services() {
    print_warning "这将删除所有容器、数据卷和日志文件（包括数据库数据）！"
    read -p "确认继续？(y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_info "操作已取消"
        exit 0
    fi
    
    print_info "清理所有服务..."
    
    if [ -d "docker/backend" ]; then
        cd docker/backend
        docker-compose down -v 2>/dev/null || true
        cd ../..
        
        # 清理后端日志
        if [ -d "docker/backend/logs" ]; then
            print_info "清理后端日志文件..."
            rm -rf docker/backend/logs/*.log 2>/dev/null || true
        fi
    fi
    
    if [ -d "docker/frontend" ]; then
        cd docker/frontend
        docker-compose down -v 2>/dev/null || true
        cd ../..
        
        # 清理前端日志
        if [ -d "docker/frontend/logs" ]; then
            print_info "清理前端日志文件..."
            rm -rf docker/frontend/logs/*.log 2>/dev/null || true
        fi
    fi
    
    # 清理根目录日志
    if [ -d "logs" ]; then
        print_info "清理根目录日志文件..."
        rm -rf logs/*.log 2>/dev/null || true
    fi
    
    # 清理悬空镜像
    print_info "清理悬空的Docker镜像..."
    docker image prune -f >/dev/null 2>&1 || true
    
    print_success "清理完成"
}

# 查看日志
show_logs() {
    print_info "请输入要查看的服务："
    echo "1) backend"
    echo "2) frontend"
    read -p "选择 [1-2]: " -n 1 -r
    echo
    
    case $REPLY in
        1)
            cd docker/backend
            docker-compose logs -f
            ;;
        2)
            cd docker/frontend
            docker-compose logs -f
            ;;
        *)
            print_error "无效选择"
            exit 1
            ;;
    esac
}

# 查看状态
show_status() {
    print_info "后端服务状态："
    if [ -d "docker/backend" ]; then
        cd docker/backend
        docker-compose ps
        cd ../..
    else
        print_warning "后端配置不存在"
    fi
    
    echo ""
    print_info "前端服务状态："
    if [ -d "docker/frontend" ]; then
        cd docker/frontend
        docker-compose ps
        cd ../..
    else
        print_warning "前端配置不存在"
    fi
}

# 主函数
main() {
    check_docker
    
    case "${1}" in
        backend)
            start_backend
            ;;
        frontend)
            start_frontend
            ;;
        stop)
            stop_services
            ;;
        clean)
            clean_services
            ;;
        logs)
            show_logs
            ;;
        status)
            show_status
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            print_error "未知选项: $1"
            echo ""
            show_help
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"
