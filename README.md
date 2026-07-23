# 高压输电塔线 GIS 地理信息系统

## 项目简介

本项目是一个基于 Spring Boot 3.x + PostgreSQL + PostGIS 的高压输电塔线 GIS 地理信息化后端系统。

## 核心特性

- ✅ **GIS 空间数据支持**: 使用 PostGIS 存储和查询地理数据
- ✅ **输电塔管理**: 完整的输电塔增删改查功能
- ✅ **输电线路管理**: 线路路径管理和相交查询
- ✅ **空间查询**: 支持范围查询、距离查询等地理空间操作
- ✅ **RESTful API**: 基于 OpenAPI 3.0 规范的 API 文档
- ✅ **缓存支持**: Redis 集成用于数据缓存

## 技术栈

- **Java**: 17
- **Spring Boot**: 3.1.5
- **Spring Data JPA**: ORM 框架
- **PostgreSQL**: 14+ 关系型数据库
- **PostGIS**: 3.x 地理信息插件
- **Hibernate Spatial**: 6.2.8 空间 ORM 支持
- **JTS**: 1.19.0 拓扑计算库
- **Redis**: 6.0+ 缓存存储
- **Springdoc OpenAPI**: 2.1.0 API 文档

## 项目结构

```
src/
├── main/
│   ├── java/com/tl/
│   │   ├── TransmissionLineGisApplication.java    # 应用启动类
│   │   ├── domain/
│   │   │   ├── entity/                           # 数据库实体
│   │   │   └── dto/                              # 数据传输对象
│   │   ├── repository/                            # 数据访问层
│   │   ├── service/                               # 业务逻辑层
│   │   └── controller/                            # 控制器层
│   └── resources/
│       └── application.yml                        # 应用配置
├── test/                                          # 测试代码
pom.xml                                            # Maven 配置
docker-compose.yml                                 # Docker Compose 配置
```

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- PostgreSQL 14+
- PostGIS 3.x
- Redis 6.0+

### 1. 启动依赖服务

```bash
docker-compose up -d
```

### 2. 初始化数据库

```bash
# 进入 PostgreSQL 容器
docker exec -it transmission-line-postgres psql -U postgres

# 在 psql 中执行
CREATE DATABASE transmission_line_gis;
\c transmission_line_gis
CREATE EXTENSION postgis;
```

### 3. 修改应用配置（可选）

编辑 `src/main/resources/application.yml`，根据需要修改数据库连接信息。

### 4. 编译和运行

```bash
# 编译项目
mvn clean package

# 运行应用
mvn spring-boot:run

# 或者使用 Java 直接运行
java -jar target/transmission-line-gis-1.0.0.jar
```

### 5. 访问应用

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **API Docs**: http://localhost:8080/api/v3/api-docs

## API 示例

### 创建输电塔

```bash
curl -X POST http://localhost:8080/api/towers \
  -H "Content-Type: application/json" \
  -d '{
    "towerCode": "T001",
    "towerName": "1号塔",
    "longitude": 120.5,
    "latitude": 31.2,
    "voltageLevel": 500,
    "towerType": "直线塔",
    "height": 45.5,
    "foundationType": "桩基",
    "lineId": 1,
    "status": 0
  }'
```

### 查询所有输电塔

```bash
curl http://localhost:8080/api/towers?page=0&size=20
```

### 查询附近的塔

```bash
curl "http://localhost:8080/api/towers/nearby?longitude=120.5&latitude=31.2&radiusKm=10&page=0&size=20"
```

## 数据库表结构

### transmission_tower (输电塔)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| tower_code | VARCHAR(50) | 塔号（唯一） |
| tower_name | VARCHAR(100) | 塔名称 |
| location | geography(Point,4326) | 地理位置 |
| voltage_level | INTEGER | 电压等级(kV) |
| tower_type | VARCHAR(50) | 塔型 |
| height | DOUBLE PRECISION | 塔高(米) |
| foundation_type | VARCHAR(50) | 基础类型 |
| line_id | BIGINT | 所属线路ID |
| status | SMALLINT | 状态(0:正常,1:维护中,2:故障) |
| description | TEXT | 描述 |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |

### transmission_line (输电线路)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| line_code | VARCHAR(100) | 线路代码（唯一） |
| line_name | VARCHAR(100) | 线路名称 |
| path | geography(LineString,4326) | 线路路径 |
| start_point | VARCHAR(100) | 起点 |
| end_point | VARCHAR(100) | 终点 |
| voltage_level | INTEGER | 电压等级(kV) |
| length | DOUBLE PRECISION | 线路长度(km) |
| circuits | INTEGER | 回路数 |
| status | SMALLINT | 状态(0:运行,1:停电,2:检修) |
| description | TEXT | 描述 |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |

## 常见问题

### Q: 如何修改坐标系统？
A: 默认使用 SRID 4326 (WGS84)，可在实体中修改 SRID 值。

### Q: 如何处理大容量的地理数据？
A: 建议为 location/path 字段创建空间索引，配置连接池大小，使用缓存策略。

## 许可证

MIT