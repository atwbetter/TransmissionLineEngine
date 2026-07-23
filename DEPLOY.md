# 部署指南

## 开发环境部署

### 前置条件

- JDK 17+
- Maven 3.8+
- Docker & Docker Compose
- Git

### 步骤 1: 克隆仓库

```bash
git clone https://github.com/atwbetter/TransmissionLineEngine.git
cd TransmissionLineEngine
git checkout develop
```

### 步骤 2: 启动基础服务

```bash
# 启动 PostgreSQL + PostGIS + Redis
docker-compose up -d

# 验证服务启动状态
docker-compose ps

# 查看日志
docker-compose logs -f postgres
```

### 步骤 3: 初始化数据库

```bash
# 进入 PostgreSQL 容器
docker exec -it transmission-line-postgres psql -U postgres

# 执行以下 SQL 命令
CREATE DATABASE transmission_line_gis;
\c transmission_line_gis
CREATE EXTENSION postgis;
CREATE EXTENSION postgis_topology;

# 退出
\q
```

### 步骤 4: 编译项目

```bash
# 清理并编译
mvn clean install -DskipTests

# 或使用包装器（推荐）
./mvnw clean install -DskipTests
```

### 步骤 5: 运行应用

```bash
# 方式 1: 使用 Maven 插件
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# 方式 2: 直接运行 JAR
java -jar target/transmission-line-gis-1.0.0.jar --spring.profiles.active=dev

# 方式 3: 使用 IDE 运行（推荐用于开发）
# 在 IDE 中右键运行 TransmissionLineGisApplication.java
```

### 步骤 6: 验证应用

```bash
# 检查应用是否启动成功
curl http://localhost:8080/api/swagger-ui.html

# 查看日志
tail -f logs/application.log
```

## 生产环境部署

### Docker 镜像构建

#### 创建 Dockerfile

```dockerfile
FROM openjdk:17-slim

WORKDIR /app

COPY target/transmission-line-gis-1.0.0.jar app.jar

ENV JAVA_OPTS="-Xmx512m -Xms256m"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### 构建镜像

```bash
# 先编译项目
mvn clean package -DskipTests

# 构建 Docker 镜像
docker build -t transmission-line-gis:1.0.0 .

# 查看镜像
docker images | grep transmission-line-gis
```

#### 运行容器

```bash
docker run -d \
  --name transmission-line-app \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/transmission_line_gis \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=yourpassword \
  -e SPRING_REDIS_HOST=redis \
  --network transmission-network \
  transmission-line-gis:1.0.0
```

### Kubernetes 部署

#### 创建部署文件 (deployment.yaml)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: transmission-line-app
  namespace: default
spec:
  replicas: 2
  selector:
    matchLabels:
      app: transmission-line-app
  template:
    metadata:
      labels:
        app: transmission-line-app
    spec:
      containers:
      - name: app
        image: transmission-line-gis:1.0.0
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_DATASOURCE_URL
          valueFrom:
            configMapKeyRef:
              name: app-config
              key: db-url
        - name: SPRING_DATASOURCE_USERNAME
          valueFrom:
            secretKeyRef:
              name: app-secret
              key: db-username
        - name: SPRING_DATASOURCE_PASSWORD
          valueFrom:
            secretKeyRef:
              name: app-secret
              key: db-password
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /api/swagger-ui.html
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /api/swagger-ui.html
            port: 8080
          initialDelaySeconds: 20
          periodSeconds: 5
```

#### 部署到 Kubernetes

```bash
# 应用配置
kubectl apply -f deployment.yaml

# 查看部署状态
kubectl get deployments
kubectl get pods

# 查看日志
kubectl logs -f deployment/transmission-line-app
```

## 环境变量配置

### 应用配置环境变量

```bash
# 数据库配置
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/transmission_line_gis
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# Redis 配置
SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379

# 应用配置
SERVER_PORT=8080
SERVER_SERVLET_CONTEXT_PATH=/api

# 日志配置
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_COM_TL=DEBUG
```

## 监控和日志

### 应用日志

日志文件位置：`logs/application.log`

查看日志：
```bash
tail -f logs/application.log
```

### 数据库监控

```bash
# 连接数据库
docker exec -it transmission-line-postgres psql -U postgres -d transmission_line_gis

# 查看表空间使用情况
\db+

# 查看索引情况
\d transmission_tower
\d+ transmission_tower

# 查询性能统计
SELECT * FROM pg_stat_statements;
```

### 健康检查

```bash
# 应用健康检查
curl http://localhost:8080/api/swagger-ui.html

# 数据库连接检查
curl -X GET http://localhost:8080/api/towers?page=0&size=1
```

## 常见问题

### Q: 如何修改数据库连接信息？
A: 修改 `src/main/resources/application.yml` 或设置环境变量

### Q: 如何启用 SQL 日志？
A: 在 `application-dev.yml` 中设置 `spring.jpa.show-sql: true`

### Q: 如何扩展应用性能？
A: 
- 增加数据库连接池大小
- 启用 Redis 缓存
- 使用负载均衡器（如 Nginx）

### Q: 如何备份数据库？
```bash
# PostgreSQL 备份
docker exec transmission-line-postgres pg_dump -U postgres transmission_line_gis > backup.sql

# 恢复
docker exec -i transmission-line-postgres psql -U postgres transmission_line_gis < backup.sql
```

## 性能调优

### JVM 参数调优

```bash
export JAVA_OPTS="-Xmx1024m -Xms512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

### PostgreSQL 调优

```sql
-- 查看当前配置
SHOW work_mem;
SHOW shared_buffers;
SHOW effective_cache_size;

-- 修改配置（需要重启数据库）
ALTER SYSTEM SET shared_buffers = '256MB';
ALTER SYSTEM SET work_mem = '8MB';
```

### 索引优化

```sql
-- 创建部分索引（仅索引正常状态的塔）
CREATE INDEX idx_tower_status ON transmission_tower(status) 
WHERE status = 0;

-- BRIN 索引（适合大表的时间序列数据）
CREATE INDEX idx_tower_created_at ON transmission_tower USING BRIN (created_at);
```
