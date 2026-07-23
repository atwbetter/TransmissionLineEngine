# 项目架构说明

## 系统架构设计

### 分层设计

```
┌─────────────────────────────────────┐
│         Controller Layer             │ API 入口层
│  (TransmissionTowerController)      │
└────────────────┬────────────────────┘
                 │
┌────────────────▼────────────────────┐
│         Service Layer               │ 业务逻辑层
│  (TransmissionTowerService)         │
└────────────────┬────────────────────┘
                 │
┌────────────────▼────────────────────┐
│         Repository Layer            │ 数据访问层
│  (TransmissionTowerRepository)      │
└────────────────┬────────────────────┘
                 │
┌────────────────▼────────────────────┐
│      PostgreSQL + PostGIS           │ 数据库层
│      (GIS 地理数据存储)              │
└─────────────────────────────────────┘
```

### 目录结构

```
src/main/java/com/tl/
├── TransmissionLineGisApplication.java
├── config/                      # 配置类
│   └── GisConfig.java
├── domain/                      # 数据模型层
│   ├── entity/                  # JPA 实体
│   │   ├── TransmissionTower.java
│   │   └── TransmissionLine.java
│   └── dto/                     # 数据传输对象
│       ├── TransmissionTowerDTO.java
│       └── TransmissionLineDTO.java
├── repository/                  # 数据访问层
│   ├── TransmissionTowerRepository.java
│   └── TransmissionLineRepository.java
├── service/                     # 业务逻辑层
│   ├── TransmissionTowerService.java
│   ├── TransmissionLineService.java
│   └── impl/
│       ├── TransmissionTowerServiceImpl.java
│       └── TransmissionLineServiceImpl.java
├── controller/                  # API 控制层
│   ├── TransmissionTowerController.java
│   └── TransmissionLineController.java
└── common/                      # 公共组件
    ├── response/
    │   └── ApiResponse.java     # 统一响应格式
    └── exception/
        └── GlobalExceptionHandler.java  # 全局异常处理
```

## 核心技术选型

### 1. Spring Boot 3.1.5
- 最新的企业级框架版本
- 原生支持 Java 17+
- 改进的错误处理和日志记录

### 2. PostgreSQL + PostGIS
- **PostgreSQL 14+**: 开源关系型数据库
  - 强大的 ACID 事务支持
  - 优秀的并发性能
  - 丰富的数据类型支持

- **PostGIS 3.x**: 地理信息系统扩展
  - 空间数据类型（Point, LineString, Polygon 等）
  - 空间索引（GiST, BRIN）
  - 空间关系查询（距离、相交、包含等）
  - WGS84 坐标系统支持（SRID 4326）

### 3. Hibernate Spatial 6.2.8
- JPA ORM 框架的地理信息扩展
- 自动映射空间数据类型
- 支持空间查询方法

### 4. JTS (Java Topology Suite) 1.19.0
- 地理空间拓扑计算库
- 提供 Geometry 类型
- 支持坐标转换和空间运算

### 5. Redis 7.x
- 分布式缓存存储
- 支持多种数据结构
- 高性能访问

## 数据流

### 新增输电塔流程

```
HTTP POST /api/towers
   ↓
TransmissionTowerController.create()
   ↓
TransmissionTowerDTO 验证
   ↓
TransmissionTowerServiceImpl.create()
   ↓
DTO → Entity 转换 (设置地理位置 Point)
   ↓
TransmissionTowerRepository.save()
   ↓
Hibernate → PostGIS
   ↓
数据库存储 (geography(Point,4326))
   ↓
Entity → DTO 转换
   ↓
HTTP 200 Response
```

### 地理空间查询流程

```
HTTP GET /api/towers/nearby?longitude=120.5&latitude=31.2&radiusKm=10
   ↓
TransmissionTowerController.getNearby()
   ↓
TransmissionTowerServiceImpl.findNearby()
   ↓
构建 WKT 格式 POINT(120.5 31.2)
   ↓
计算查询半径 (radiusKm → 米)
   ↓
TransmissionTowerRepository.findTowersNear()
   ↓
PostGIS ST_DWithin() 空间查询
   ↓
返回结果集 → DTO 转换
   ↓
HTTP 200 分页结果
```

## GIS 空间查询支持

### 支持的查询类型

1. **点查询 (Point Query)**
   - 根据 GPS 坐标查询输电塔
   - 使用 `ST_Equals()` 或 `ST_DWithin()` 函数

2. **距离查询 (Distance Query)**
   - 查询指定半径内的塔
   - 使用 `ST_DWithin(geometry, geometry, distance)` 函数
   - 例：查询方圆 5km 内的塔

3. **范围查询 (Bounding Box Query)**
   - 查询矩形范围内的塔
   - 使用 `ST_Contains()` 函数

4. **线路相交查询 (Intersection Query)**
   - 查询与线路相交的设施
   - 使用 `ST_Intersects()` 函数

5. **缓冲区查询 (Buffer Query)**
   - 查询线路周围缓冲区内的对象
   - 使用 `ST_Buffer()` 和 `ST_Intersects()` 组合

## 数据库设计

### 输电塔表 (transmission_tower)

```sql
CREATE TABLE transmission_tower (
  id BIGSERIAL PRIMARY KEY,
  tower_code VARCHAR(50) UNIQUE NOT NULL,
  tower_name VARCHAR(100),
  location geography(Point, 4326),
  voltage_level INTEGER,
  tower_type VARCHAR(50),
  height DOUBLE PRECISION,
  foundation_type VARCHAR(50),
  line_id BIGINT,
  status SMALLINT DEFAULT 0,
  description TEXT,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_tower_location ON transmission_tower USING GIST(location);
CREATE INDEX idx_tower_voltage ON transmission_tower(voltage_level);
```

### 输电线路表 (transmission_line)

```sql
CREATE TABLE transmission_line (
  id BIGSERIAL PRIMARY KEY,
  line_code VARCHAR(100) UNIQUE NOT NULL,
  line_name VARCHAR(100) NOT NULL,
  path geography(LineString, 4326),
  start_point VARCHAR(100),
  end_point VARCHAR(100),
  voltage_level INTEGER,
  length DOUBLE PRECISION,
  circuits INTEGER,
  status SMALLINT DEFAULT 0,
  description TEXT,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_line_path ON transmission_line USING GIST(path);
CREATE INDEX idx_line_code ON transmission_line(line_code);
```

## API 设计规范

### 响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1690000000000
}
```

### 错误响应

```json
{
  "code": 400,
  "message": "参数错误或业务逻辑错误",
  "timestamp": 1690000000000
}
```

## 性能优化建议

### 1. 数据库优化
- 为地理位置字段创建 GiST 或 BRIN 索引
- 使用 geography 类型而非 geometry（自动处理球面距离）
- 定期执行 ANALYZE 更新统计信息

### 2. 缓存策略
- 使用 Redis 缓存频繁查询的数据
- 设置适当的缓存过期时间
- 热数据优先级缓存

### 3. 连接池配置
- 根据并发数调整 HikariCP 连接池大小
- 监控连接池使用情况

### 4. 查询优化
- 使用分页查询大数据集
- 避免 N+1 查询问题
- 适当使用查询投影减少数据传输

## 扩展点

### 1. 集成地图服务
- 集成 GeoServer 提供 WMS/WFS 服务
- 前端使用 Leaflet/MapBox 进行可视化

### 2. 实时监控
- 集成消息队列（Kafka/RabbitMQ）
- 实现输电塔状态实时推送

### 3. 高级分析
- 使用 PostGIS 进行复杂空间分析
- 集成 Python 进行数据分析和预测

### 4. 权限控制
- 实现细粒度的权限管理
- 支持基于角色的访问控制（RBAC）
