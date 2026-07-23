# 高压输电塔线 GIS 地理信息系统

## 项目简介

本项目是一个基于 Spring Boot 3.x + PostgreSQL + PostGIS 的高压输电塔线 GIS 地理信息化后端系统。

## 核心特性

- ✅ **GIS 空间数据支持**: 使用 PostGIS 存储和查询地理数据

## 技术栈

- **Java**: 17
- **Spring Boot**: 3.1.5


## 项目结构

```
src/
├── main/
│   ├── java/com/tl/
│   │   ├── TransmissionLineGisApplication.java    # 应用启动类
│   │   ├── domain/
│   │   │   ├── entity/                           # 数据库实体
│   │   │   └── dto/                              # 数据传输对象
│   │   ├── mapper/                               # 数据访问层
│   │   ├── service/                               # 业务逻辑层
│   │   └── controller/                            # 控制器层
│   └── resources/
│       └── application.yml                        # 应用配置
├── test/                                          # 测试代码
pom.xml                                            # Maven 配置
docker-compose.yml                                 # Docker Compose 配置
```

