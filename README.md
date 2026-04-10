# 庙观管理系统

## 项目基础信息

- 项目类型：采用vue3 + springboot3开发的前后端分离的庙观在线报名系统
- 前端核心技术栈：vue 3.2.5 + vue-router 4.0.13 + pinia 3.0.4 + axios 1.13.6 + element-plus 2.13.3 + unocss 66.6.5 + typescript 5.9.3
- 后端核心技术栈：springboot 3.2.4 + spring-security 6.2.3 + jwt 0.12.3 + spring boot jpa 3.2.4 + mysql8.0.33
- 项目用途：庙观工作人员登记各类报名信息的管理系统
- 核心约束：禁止使用 any 类型、所有接口需加 Token 权限校验、前端开发遵循vue3 函数式编程规则

## 目录结构
- temple-common：是后端的公共模块
- temple-system: 是管理系统的后台接口
- temple-system-web:是管理系统后台的前端代码

## 环境搭建

### 传统开发环境
- 前端开发：`npm run dev`
- 后端开发：直接运行根目录下的 `run.sh` 文件

### Docker 部署（推荐）

项目提供完整的 Docker 配置，支持一键部署：

**方式一：仅启动后端服务（开发环境）**
```bash
# 使用快捷脚本
./docker.sh backend

# 或手动执行
cd docker/backend
docker-compose up -d
```

**方式二：启动完整服务（生产环境）**
```bash
# 使用快捷脚本
./docker.sh frontend

# 或手动执行
cd docker/frontend
docker-compose up -d
```

**快捷脚本用法：**
```bash
./docker.sh backend    # 启动后端服务
./docker.sh frontend   # 启动完整服务
./docker.sh stop       # 停止所有服务
./docker.sh logs       # 查看日志
./docker.sh status     # 查看状态
./docker.sh clean      # 清理所有数据
./docker.sh help       # 查看帮助
```

**访问地址：**
- 前端应用：http://localhost:80（仅完整服务）
- 后端 API：http://localhost:8080/system
- API 文档：http://localhost:8080/system/doc.html

详细说明请查看 [docker/README.md](docker/README.md) 