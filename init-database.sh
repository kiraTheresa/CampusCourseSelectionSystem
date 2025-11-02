#!/bin/bash

# 校园选课管理系统 - 数据库初始化脚本
# 用于创建数据库并初始化基础表结构

set -e  # 遇到错误立即退出

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 配置变量（可通过环境变量覆盖）
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-3306}"
DB_NAME="${DB_NAME:-campus_course_selection_system}"
DB_USER="${DB_USER:-root}"
DB_PASSWORD="${DB_PASSWORD:-root}"
DB_CHARSET="${DB_CHARSET:-utf8mb4}"
DB_COLLATE="${DB_COLLATE:-utf8mb4_unicode_ci}"

# 脚本目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SCHEMA_FILE="${SCRIPT_DIR}/src/main/resources/db/schema.sql"
DATA_FILE="${SCRIPT_DIR}/src/main/resources/db/data.sql"

# 打印带颜色的消息
print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查 MySQL 是否安装
check_mysql() {
    if ! command -v mysql &> /dev/null; then
        print_error "MySQL 客户端未安装，请先安装 MySQL"
        exit 1
    fi
    print_info "MySQL 客户端检查通过"
}

# 测试数据库连接
test_connection() {
    print_info "测试数据库连接..."
    if mysql -h"${DB_HOST}" -P"${DB_PORT}" -u"${DB_USER}" -p"${DB_PASSWORD}" -e "SELECT 1;" &> /dev/null; then
        print_info "数据库连接成功"
        return 0
    else
        print_error "数据库连接失败，请检查配置"
        return 1
    fi
}

# 创建数据库
create_database() {
    print_info "创建数据库: ${DB_NAME}"
    
    mysql -h"${DB_HOST}" -P"${DB_PORT}" -u"${DB_USER}" -p"${DB_PASSWORD}" <<EOF
CREATE DATABASE IF NOT EXISTS \`${DB_NAME}\`
    CHARACTER SET ${DB_CHARSET}
    COLLATE ${DB_COLLATE};
EOF

    if [ $? -eq 0 ]; then
        print_info "数据库创建成功"
    else
        print_error "数据库创建失败"
        exit 1
    fi
}

# 执行 schema.sql
execute_schema() {
    if [ ! -f "${SCHEMA_FILE}" ]; then
        print_warning "Schema 文件不存在: ${SCHEMA_FILE}"
        print_warning "将跳过表结构初始化（JPA 会自动创建表）"
        return
    fi

    print_info "执行表结构初始化: ${SCHEMA_FILE}"
    
    mysql -h"${DB_HOST}" -P"${DB_PORT}" -u"${DB_USER}" -p"${DB_PASSWORD}" "${DB_NAME}" < "${SCHEMA_FILE}"

    if [ $? -eq 0 ]; then
        print_info "表结构初始化成功"
    else
        print_warning "表结构初始化可能失败，但 JPA 会自动创建表结构"
    fi
}

# 执行 data.sql（可选）
execute_data() {
    if [ ! -f "${DATA_FILE}" ]; then
        print_warning "Data 文件不存在: ${DATA_FILE}"
        return
    fi

    print_info "执行测试数据初始化: ${DATA_FILE}"
    
    mysql -h"${DB_HOST}" -P"${DB_PORT}" -u"${DB_USER}" -p"${DB_PASSWORD}" "${DB_NAME}" < "${DATA_FILE}"

    if [ $? -eq 0 ]; then
        print_info "测试数据初始化成功"
    else
        print_warning "测试数据初始化失败"
    fi
}

# 显示数据库信息
show_info() {
    print_info "数据库初始化完成！"
    echo ""
    echo "数据库信息："
    echo "  - 主机: ${DB_HOST}"
    echo "  - 端口: ${DB_PORT}"
    echo "  - 数据库名: ${DB_NAME}"
    echo "  - 用户名: ${DB_USER}"
    echo "  - 字符集: ${DB_CHARSET}"
    echo ""
    echo "下一步："
    echo "  1. 配置应用连接数据库（修改 application-prod.yml）"
    echo "  2. 启动应用: mvn spring-boot:run -Dspring-boot.run.profiles=prod"
    echo "  3. 或设置环境变量: export SPRING_PROFILES_ACTIVE=prod"
}

# 主函数
main() {
    echo "=========================================="
    echo "  校园选课管理系统 - 数据库初始化"
    echo "=========================================="
    echo ""

    # 检查 MySQL
    check_mysql

    # 测试连接
    if ! test_connection; then
        print_error "请检查数据库配置或确保 MySQL 服务已启动"
        exit 1
    fi

    # 创建数据库
    create_database

    # 执行 schema
    execute_schema

    # 询问是否执行测试数据
    echo ""
    read -p "是否导入测试数据？(y/N): " -n 1 -r
    echo ""
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        execute_data
    else
        print_info "跳过测试数据导入"
    fi

    # 显示信息
    echo ""
    show_info
}

# 显示帮助信息
show_help() {
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  -h, --help          显示帮助信息"
    echo "  -H, --host HOST     数据库主机 (默认: localhost)"
    echo "  -P, --port PORT     数据库端口 (默认: 3306)"
    echo "  -d, --database NAME 数据库名称 (默认: campus_course_selection_system)"
    echo "  -u, --user USER     数据库用户名 (默认: root)"
    echo "  -p, --password PASS 数据库密码 (默认: root)"
    echo ""
    echo "环境变量:"
    echo "  也可以通过环境变量设置："
    echo "    DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD"
    echo ""
    echo "示例:"
    echo "  $0"
    echo "  $0 -u admin -p mypassword"
    echo "  DB_PASSWORD=mypassword $0"
}

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        -H|--host)
            DB_HOST="$2"
            shift 2
            ;;
        -P|--port)
            DB_PORT="$2"
            shift 2
            ;;
        -d|--database)
            DB_NAME="$2"
            shift 2
            ;;
        -u|--user)
            DB_USER="$2"
            shift 2
            ;;
        -p|--password)
            DB_PASSWORD="$2"
            shift 2
            ;;
        *)
            print_error "未知参数: $1"
            show_help
            exit 1
            ;;
    esac
done

# 运行主函数
main

