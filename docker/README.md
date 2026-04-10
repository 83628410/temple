# Temple Docker 完整部署指南

本目录包含 Temple 项目的前后端 Docker 配置文件，提供完整的容器化部署方案。

## 设置 Docker 源

在 Docker 配置文件中添加镜像加速器（`/etc/docker/daemon.json`）：

```json
{
  "registry-mirrors": [
    "https://docker.xuanyuan.me",
    "https://docker.1ms.run",
    "https://docker.m.daocloud.io",
    "https://mirror.baidubce.com"
  ]
}
```

配置后重启 Docker 服务：
```bash
sudo systemctl daemon-reload
sudo systemctl restart docker
```

## 目录结构

```
docker/
├── .dockerignore              # Docker 构建忽略文件
├── README.md                  # 本文件 - 完整部署指南
├── backend/                   # 后端 Docker 配置
│   ├── Dockerfile             # 后端应用镜像构建文件
│   ├── docker-compose.yml     # 后端服务编排（MySQL + Redis + Backend）
│   ├── mysql/
│   │   └── conf/
│   │       └── mysql.cnf      # MySQL 配置文件
│   └── redis/
│       └── conf/
│           └── redis.conf     # Redis 配置文件
└── frontend/                  # 前端 Docker 配置
    ├── Dockerfile             # 前端应用镜像构建文件
    ├── docker-compose.yml     # 前端服务编排（Frontend + Backend + MySQL + Redis）
    ├── .dockerignore          # 前端构建忽略文件
    └── nginx.conf             # Nginx 配置文件
```

## 部署方式

项目提供两种部署方式：

### 自动清理机制

**重要提示：** 使用 `docker.sh` 脚本启动服务时，系统会自动：
1. 停止并删除所有旧的容器
2. 删除所有数据卷（包括数据库数据）
3. 清理所有日志文件（后端、前端、根目录）
4. 清理悬空的 Docker 镜像
5. 重新构建最新镜像
6. 启动全新服务

这确保每次启动都是最新代码和干净的环境，数据库会重新执行 `sql/init.sql` 初始化。

**清理的日志文件包括：**
- `docker/backend/logs/*.log` - 后端应用日志
- `docker/frontend/logs/*.log` - 前端应用日志（如有）
- `logs/*.log` - 根目录日志文件（如有）

### 方式一：仅后端服务（推荐开发环境）

只启动后端服务（MySQL + Redis + Backend），前端使用开发服务器。

**使用快捷脚本：**
```bash
./docker.sh backend
```

**或手动执行：**
```bash
cd docker/backend
docker-compose up -d --build
```

**访问：**
- 后端 API：http://localhost:8080/system
- API 文档：http://localhost:8080/system/doc.html

### 方式二：完整服务（推荐生产环境）

启动完整的前后端服务（Frontend + Backend + MySQL + Redis）。

**使用快捷脚本：**
```bash
./docker.sh frontend
```

**或手动执行：**
```bash
cd docker/frontend
docker-compose up -d --build
```

**访问：**
- 前端应用：http://localhost:80
- 后端 API：http://localhost:8080/system
- API 文档：http://localhost:8080/system/doc.html

## 快捷脚本使用

项目根目录的 `docker.sh` 脚本提供了便捷的管理功能：

```bash
# 启动后端服务（自动清理旧容器、卷、镜像和日志）
./docker.sh backend

# 启动完整服务（自动清理旧容器、卷、镜像和日志）
./docker.sh frontend

# 停止所有服务
./docker.sh stop

# 查看服务日志
./docker.sh logs

# 查看服务状态
./docker.sh status

# 清理所有容器、数据卷和日志
./docker.sh clean

# 查看帮助
./docker.sh help
```

## 常用命令

### 停止服务

```bash
# 后端服务
cd docker/backend
docker-compose stop

# 前端服务
cd docker/frontend
docker-compose stop
```

### 停止并删除容器

```bash
# 后端服务
cd docker/backend
docker-compose down

# 前端服务
cd docker/frontend
docker-compose down
```

### 停止并删除容器及数据卷（会删除数据库数据）

```bash
docker-compose down -v
```

### 重新构建并启动

```bash
# 后端服务
cd docker/backend
docker-compose up -d --build

# 前端服务
cd docker/frontend
docker-compose up -d --build
```

### 重启特定服务

```bash
# 后端服务
docker-compose restart temple-system
docker-compose restart mysql
docker-compose restart redis

# 前端服务
docker-compose restart temple-web
```

### 进入容器

```bash
# 进入后端应用容器
docker exec -it temple-system sh

# 进入前端容器
docker exec -it temple-web sh

# 进入 MySQL 容器
docker exec -it temple-mysql mysql -uroot -p111111

# 进入 Redis 容器
docker exec -it temple-redis redis-cli
```

## 配置说明

### 环境变量

在 `docker-compose.yml` 中可以修改以下配置：

#### MySQL
- `MYSQL_ROOT_PASSWORD`: root 用户密码（默认：111111）
- `MYSQL_DATABASE`: 初始创建的数据库名（默认：temple_system）

#### Redis
- 默认无密码，如需设置密码请修改 `redis/conf/redis.conf` 中的 `requirepass`

#### 应用
- `SPRING_DATASOURCE_URL`: 数据库连接 URL
- `SPRING_DATASOURCE_USERNAME`: 数据库用户名
- `SPRING_DATASOURCE_PASSWORD`: 数据库密码
- `SPRING_DATA_REDIS_HOST`: Redis 主机地址
- `SPRING_DATA_REDIS_PORT`: Redis 端口

### 数据持久化

所有数据都通过 Docker volumes 持久化：

- `mysql_data`: MySQL 数据目录
- `redis_data`: Redis 数据目录
- `./logs`: 应用日志目录（映射到宿主机）

## 数据库初始化

`sql/init.sql` 文件会在 MySQL 容器首次启动时自动执行。

如果需要重新初始化数据库：

```bash
# 1. 停止并删除容器及数据卷
docker-compose down -v

# 2. 重新启动
docker-compose up -d --build
```

## 健康检查

所有服务都配置了健康检查：

- **MySQL**: 使用 `mysqladmin ping` 检查
- **Redis**: 使用 `redis-cli ping` 检查
- **temple-system**: 使用 actuator health 端点检查
- **temple-web**: 使用 wget 检查 HTTP 服务

应用服务会等待 MySQL 和 Redis 健康检查通过后才启动。

## 故障排查

### 1. 应用启动失败

```bash
# 查看后端应用日志
cd docker/backend
docker-compose logs temple-system

# 查看前端应用日志
cd docker/frontend
docker-compose logs temple-web

# 检查 MySQL 和 Redis 是否正常运行
docker-compose ps
```

### 2. 数据库连接失败

```bash
# 检查 MySQL 日志
docker-compose logs mysql

# 进入 MySQL 容器检查
docker exec -it temple-mysql mysql -uroot -p111111
```

### 3. Redis 连接失败

```bash
# 检查 Redis 日志
docker-compose logs redis

# 测试 Redis 连接
docker exec -it temple-redis redis-cli ping
```

### 4. 重新初始化数据库

```bash
# 停止服务
cd docker/backend  # 或 cd docker/frontend
docker-compose down

# 删除数据卷（注意：会删除所有数据）
docker volume rm temple_mysql_data

# 重新启动
docker-compose up -d --build
```

### 5. 端口冲突

如果端口被占用，可以修改 `docker-compose.yml` 中的端口映射：

```yaml
ports:
  - "8081:8080"  # 将宿主机的 8081 映射到容器的 8080
```

## 生产环境建议

### 1. 修改默认密码

```yaml
# docker-compose.yml
environment:
  MYSQL_ROOT_PASSWORD: your-strong-password
  SPRING_DATASOURCE_PASSWORD: your-strong-password
```

在 `redis/conf/redis.conf` 中设置密码：
```conf
requirepass your-redis-password
```

### 2. 网络安全

- 不要将数据库和 Redis 端口暴露到公网
- 使用防火墙限制访问
- 移除 `docker-compose.yml` 中不必要的 `ports` 配置

```yaml
# 仅暴露必要端口
ports:
  - "80:80"        # 前端
  # - "3306:3306"  # 生产环境注释掉
  # - "6379:6379"  # 生产环境注释掉
```

### 3. 数据备份

```bash
# 备份 MySQL 数据
docker exec temple-mysql mysqldump -uroot -p111111 temple_system > backup_$(date +%Y%m%d).sql

# 备份 Redis 数据
docker exec temple-redis redis-cli BGSAVE

# 恢复 MySQL 数据
docker exec -i temple-mysql mysql -uroot -p111111 temple_system < backup_20240101.sql
```

### 4. 资源限制

在 `docker-compose.yml` 中添加资源限制：

```yaml
services:
  temple-system:
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
        reservations:
          cpus: '1'
          memory: 1G
```

### 5. 日志管理

- 定期清理日志文件
- 使用日志轮转工具
- 配置日志文件大小限制

```yaml
# docker-compose.yml
logging:
  driver: "json-file"
  options:
    max-size: "100m"
    max-file: "10"
```

### 6. 使用 .env 文件管理敏感信息

创建 `.env` 文件：
```env
MYSQL_ROOT_PASSWORD=your-password
SPRING_DATASOURCE_PASSWORD=your-password
REDIS_PASSWORD=your-redis-password
```

在 `docker-compose.yml` 中引用：
```yaml
environment:
  MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
```

## 更新应用

当代码有更新时：

```bash
# 更新后端
cd docker/backend
docker-compose up -d --build temple-system

# 更新前端
cd docker/frontend
docker-compose up -d --build temple-web

# 或使用快捷脚本（会自动清理并重建）
./docker.sh backend
./docker.sh frontend
```

## 清理

### 使用快捷脚本（推荐）

```bash
# 清理所有容器、数据卷、镜像和日志
./docker.sh clean
```

### 手动清理

```bash
# 停止并删除所有容器、网络
cd docker/backend  # 或 cd docker/frontend
docker-compose down

# 删除所有容器、网络、数据卷
docker-compose down -v

# 删除构建的镜像
docker-compose down --rmi all

# 清理所有悬空镜像
docker image prune -f

# 清理日志文件
rm -rf docker/backend/logs/*.log 2>/dev/null || true
rm -rf docker/frontend/logs/*.log 2>/dev/null || true
rm -rf logs/*.log 2>/dev/null || true

# 清理所有未使用的镜像、容器、网络和数据卷
docker system prune -a --volumes
```

## 注意事项

1. **数据库初始化**：`sql/init.sql` 会在 MySQL 容器首次启动时自动执行
2. **数据持久化**：数据库和 Redis 数据通过 Docker volumes 持久化
3. **健康检查**：所有服务都配置了健康检查，确保依赖关系正确
4. **时区设置**：所有容器统一使用 Asia/Shanghai 时区
5. **自动清理**：使用 `docker.sh` 脚本启动时会自动清理旧数据
6. **生产环境**：建议直接使用 `docker-compose` 命令，保留数据卷

## 技术支持

如遇到问题，请检查：
1. Docker 和 Docker Compose 版本是否符合要求
2. 端口是否被占用
3. 日志中的错误信息
4. 数据库和 Redis 是否正常启动

查看日志：
```bash
./docker.sh logs
# 或
docker-compose logs -f
```
