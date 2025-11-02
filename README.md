# 校园选课管理系统

## 项目说明

校园选课管理系统是一个基于Spring Boot开发的后端API服务，提供完整的课程管理、学生管理和选课管理功能。系统采用RESTful API设计风格，支持课程的增删改查、学生信息管理、选课退课、成绩录入等核心业务功能。

**当前版本**: 1.1（支持 MySQL 数据库和多环境配置）

### 主要功能模块

1. **课程管理**
   - 创建、查询、更新、删除课程
   - 查看课程详情和选课人数
   - 查询有剩余容量的课程

2. **学生管理**
   - 学生信息的增删改查
   - 学号唯一性校验
   - 按专业、年级筛选学生

3. **选课管理**
   - 学生选课和退课
   - 查询学生选课记录
   - 更新选课状态和成绩
   - 支持选课状态枚举（ENROLLED, WITHDRAWN, COMPLETED, FAILED）

## 技术栈

- **后端框架**: Spring Boot 3.4.10
- **ORM框架**: Spring Data JPA
- **数据库**: 
  - 开发环境：H2（内存数据库）
  - 生产环境：MySQL 8.0+
- **构建工具**: Maven
- **Java版本**: JDK 17

## 如何运行项目

### 前置条件

- JDK 17 或更高版本
- Maven 3.6 或更高版本
- MySQL 8.0 或更高版本（生产环境）
- H2 数据库（开发环境，已包含在依赖中）

### 环境配置

#### 1. 克隆项目代码

```bash
git clone <repository-url>
cd CampusCourseSelectionSystem
```

#### 2. 数据库配置

项目支持多环境配置，默认使用开发环境（H2内存数据库）。

##### 开发环境（H2）- 默认

无需额外配置，应用启动时会自动使用 H2 内存数据库。

- H2 控制台访问地址：`http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- 用户名: `sa`
- 密码: （空）

##### 生产环境（MySQL）

1. 创建数据库：
```sql
CREATE DATABASE campus_course_selection_system 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;
```

2. 配置环境变量或修改 `application-prod.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/campus_course_selection_system?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8mb4
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:root}
```

3. 启动时指定生产环境配置：
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

或设置环境变量：
```bash
export SPRING_PROFILES_ACTIVE=prod
mvn spring-boot:run
```

#### 3. 环境切换

- **开发环境**（默认）: 使用 `application-dev.yml`，H2 内存数据库
- **生产环境**: 使用 `application-prod.yml`，MySQL 数据库

通过以下方式切换：
- 环境变量: `SPRING_PROFILES_ACTIVE=prod`
- 启动参数: `-Dspring.profiles.active=prod`
- `application.yml` 中的 `spring.profiles.active` 配置

### 启动项目

#### 方法一：使用Maven命令

```bash
# 开发环境（默认）
mvn spring-boot:run

# 生产环境
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

#### 方法二：打包后运行

```bash
mvn clean package
java -jar target/CampusCourseSelectionSystem-0.0.1-SNAPSHOT.jar

# 生产环境
java -jar target/CampusCourseSelectionSystem-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### 访问系统

- API接口地址：`http://localhost:8080/api`
- 健康检查接口：`http://localhost:8080/health`
- 数据库健康检查：`http://localhost:8080/health/db`
- H2控制台（仅开发环境）：`http://localhost:8080/h2-console`

### 数据库健康检查

系统提供了数据库健康检查接口，用于验证数据库连接状态：

```bash
# 检查数据库连接
curl http://localhost:8080/health/db
```

响应示例：
```json
{
  "status": "UP",
  "message": "数据库连接正常",
  "connection": "ok",
  "jpa": "ok",
  "repositories": {
    "course": "ok",
    "student": "ok",
    "enrollment": "ok"
  },
  "statistics": {
    "courses": 5,
    "students": 10,
    "enrollments": 15
  },
  "database": {
    "url": "jdbc:h2:mem:testdb",
    "driver": "H2 JDBC Driver"
  },
  "timestamp": 1234567890
}
```

## API 接口列表

### 课程管理接口

| 接口路径 | 方法 | 功能描述 |
|---------|------|---------|
| `/api/courses` | `GET` | 查询所有课程 |
| `/api/courses` | `POST` | 创建新课程 |
| `/api/courses/{id}` | `GET` | 查询课程详情 |
| `/api/courses/{id}` | `PUT` | 更新课程信息 |
| `/api/courses/{id}` | `DELETE` | 删除课程 |
| `/api/courses/available` | `GET` | 查询有剩余容量的课程 |

### 学生管理接口

| 接口路径 | 方法 | 功能描述 |
|---------|------|---------|
| `/api/students` | `GET` | 查询所有学生 |
| `/api/students` | `POST` | 创建新学生 |
| `/api/students/{id}` | `GET` | 查询学生详情 |
| `/api/students/{id}` | `PUT` | 更新学生信息 |
| `/api/students/{id}` | `DELETE` | 删除学生 |

### 选课管理接口

| 接口路径 | 方法 | 功能描述 |
|---------|------|---------|
| `/api/enrollments` | `POST` | 学生选课 |
| `/api/enrollments/student/{studentId}` | `GET` | 查询学生选课记录 |
| `/api/enrollments/course/{courseId}` | `GET` | 查询课程选课列表 |
| `/api/enrollments/{id}/status` | `PUT` | 更新选课状态 |
| `/api/enrollments/{id}/grade` | `PUT` | 更新成绩 |
| `/api/enrollments/{id}` | `DELETE` | 退课 |

### 健康检查接口

| 接口路径 | 方法 | 功能描述 |
|---------|------|---------|
| `/health` | `GET` | 应用健康检查 |
| `/health/db` | `GET` | 数据库健康检查 |

## 数据库初始化

### 自动初始化（开发环境）

开发环境使用 H2 数据库时，系统会自动执行初始化脚本：
- `src/main/resources/db/schema.sql` - 表结构（JPA会自动创建，此文件为参考）
- `src/main/resources/db/data.sql` - 测试数据

### 手动初始化（生产环境）

生产环境使用 MySQL 时，建议：

1. **使用 JPA 自动创建表结构**（推荐）：
   - 设置 `spring.jpa.hibernate.ddl-auto=update`（首次部署）
   - 生产环境建议使用 `validate` 并通过迁移工具管理

2. **手动执行 SQL 脚本**：
```bash
mysql -u root -p campus_course_selection_system < src/main/resources/db/schema.sql
mysql -u root -p campus_course_selection_system < src/main/resources/db/data.sql
```

3. **使用数据库迁移工具**（推荐）：
   - Flyway
   - Liquibase

## 版本更新记录

### v1.1 (2024)

- ✅ 将模拟数据库替换为 MySQL 数据库
- ✅ 添加 H2 数据库支持（开发环境）
- ✅ 实现多环境配置（dev/prod）
- ✅ 完善 JPA 实体注解和索引优化
- ✅ 添加选课状态枚举类型
- ✅ 完善 Repository 查询方法
- ✅ Service 层添加事务管理
- ✅ 添加数据库健康检查接口
- ✅ 优化异常处理机制

### v1.0 (2024)

- 初始版本
- 基本的 CRUD 功能
- 模拟数据库实现

## 测试说明

### 测试文档

项目提供了两种格式的测试文档：

1. **Markdown格式测试文档** - `API测试文档.md`
   - 详细记录了所有测试用例、预期结果和实际结果
   - 包含测试结果统计和分析

2. **HTTP格式测试文件** - `API测试.http`
   - 支持在IntelliJ IDEA或VSCode REST Client插件中直接运行
   - 便于快速验证API功能

### 运行测试

#### 使用HTTP文件进行测试

1. 在IntelliJ IDEA中安装HTTP Client插件
2. 打开 `API测试.http` 文件
3. 点击每个请求左侧的运行按钮执行测试

#### 数据库健康检查测试

```bash
curl http://localhost:8080/health/db
```

## 注意事项

1. 所有API响应均采用统一格式：`{"code": 状态码, "message": "消息", "data": 数据}`
2. 自动生成字段（如id、创建时间）在创建请求中不需要提供
3. 更新操作支持部分字段更新
4. 系统对关键数据进行唯一性校验和业务规则验证
5. 选课状态使用枚举类型：`ENROLLED`, `WITHDRAWN`, `COMPLETED`, `FAILED`
6. 开发环境数据存储在内存中，应用重启后数据会丢失
7. 生产环境请确保数据库连接配置正确，建议使用连接池配置

## 故障排除

### 数据库连接问题

1. 检查数据库是否启动
2. 验证数据库连接配置（用户名、密码、URL）
3. 检查数据库是否已创建
4. 使用健康检查接口验证：`curl http://localhost:8080/health/db`

### H2 控制台无法访问

- 确认使用开发环境配置
- 检查 `application-dev.yml` 中的 H2 控制台配置
- 访问地址：`http://localhost:8080/h2-console`

## License

MIT
