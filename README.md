# 校园选课管理系统

## 项目说明

校园选课管理系统是一个基于Spring Boot开发的后端API服务，提供完整的课程管理、学生管理和选课管理功能。系统采用RESTful API设计风格，支持课程的增删改查、学生信息管理、选课退课、成绩录入等核心业务功能。

### 主要功能模块

1. **课程管理**
   - 创建、查询、更新、删除课程
   - 查看课程详情和选课人数

2. **学生管理**
   - 学生信息的增删改查
   - 学号唯一性校验

3. **选课管理**
   - 学生选课和退课
   - 查询学生选课记录
   - 更新选课状态和成绩

## 技术栈

- **后端框架**: Spring Boot 2.x
- **ORM框架**: Spring Data JPA
- **数据库**: MySQL/PostgreSQL
- **API文档**: OpenAPI 3.0 (Swagger)
- **构建工具**: Maven

## 如何运行项目

### 前置条件

- JDK 1.8 或更高版本
- Maven 3.6 或更高版本
- MySQL 8.0 或 PostgreSQL 10 或更高版本

### 环境配置

1. 克隆项目代码

2. 配置数据库连接
   - 修改 `src/main/resources/application.properties` 或 `application.yml` 文件中的数据库连接信息

3. 创建数据库
   ```sql
   CREATE DATABASE campus_course_selection;
   ```

### 启动项目

#### 方法一：使用Maven命令

```bash
mvn spring-boot:run
```

#### 方法二：打包后运行

```bash
mvn clean package
java -jar target/CampusCourseSelectionSystem-1.0.jar
```

### 访问系统

- 系统启动后，API接口地址：`http://localhost:8080/api`
- OpenAPI文档地址：`http://localhost:8080/swagger-ui.html`

## API 接口列表

### 课程管理接口

| 接口路径 | 方法 | 功能描述 | 请求体 (JSON) | 成功响应 (200 OK) |
|---------|------|---------|--------------|-------------------|
| `/api/courses` | `GET` | 查询所有课程 | N/A | `{"code": 200, "message": "成功", "data": [{课程对象列表}]}` |
| `/api/courses` | `POST` | 创建新课程 | `{"code": "CS101", "title": "课程标题", "instructorId": "教师ID", "scheduleId": "排课ID", "capacity": 60}` | `{"code": 201, "message": "课程创建成功", "data": {课程对象}}` |
| `/api/courses/{id}` | `GET` | 查询课程详情 | N/A | `{"code": 200, "message": "成功", "data": {课程对象}}` |
| `/api/courses/{id}` | `PUT` | 更新课程信息 | `{"title": "更新标题", "capacity": 80}` | `{"code": 200, "message": "课程更新成功", "data": {课程对象}}` |
| `/api/courses/{id}` | `DELETE` | 删除课程 | N/A | `{"code": 200, "message": "课程删除成功", "data": null}` |

### 学生管理接口

| 接口路径 | 方法 | 功能描述 | 请求体 (JSON) | 成功响应 (200 OK) |
|---------|------|---------|--------------|-------------------|
| `/api/students` | `GET` | 查询所有学生 | N/A | `{"code": 200, "message": "成功", "data": [{学生对象列表}]}` |
| `/api/students` | `POST` | 创建新学生 | `{"studentId": "2024001", "name": "学生姓名", "major": "专业", "grade": 2024, "email": "邮箱"}` | `{"code": 201, "message": "学生创建成功", "data": {学生对象}}` |
| `/api/students/{id}` | `GET` | 查询学生详情 | N/A | `{"code": 200, "message": "成功", "data": {学生对象}}` |
| `/api/students/{id}` | `PUT` | 更新学生信息 | `{"name": "更新姓名", "email": "更新邮箱"}` | `{"code": 200, "message": "学生更新成功", "data": {学生对象}}` |
| `/api/students/check-student-id/{studentId}` | `GET` | 检查学号是否存在 | N/A | `{"code": 200, "message": "成功", "data": {"exists": true/false, "message": "提示信息"}}` |

### 选课管理接口

| 接口路径 | 方法 | 功能描述 | 请求体 (JSON) | 成功响应 (200 OK) |
|---------|------|---------|--------------|-------------------|
| `/api/enrollments` | `POST` | 学生选课 | `{"courseId": "课程ID", "studentId": "学生ID"}` | `{"code": 201, "message": "选课成功", "data": {选课记录}}` |
| `/api/enrollments/student/{studentId}` | `GET` | 查询学生选课记录 | N/A | `{"code": 200, "message": "成功", "data": [{选课记录列表}]}` |
| `/api/enrollments/course/{courseId}` | `GET` | 查询课程选课列表 | N/A | `{"code": 200, "message": "成功", "data": [{选课记录列表}]}` |
| `/api/enrollments/{id}/status` | `PUT` | 更新选课状态 | `{"status": "COMPLETED"}` | `{"code": 200, "message": "状态更新成功", "data": {选课记录}}` |
| `/api/enrollments/{id}/grade` | `PUT` | 更新成绩 | `{"grade": 85.5}` | `{"code": 200, "message": "成绩更新成功", "data": {选课记录}}` |
| `/api/enrollments/{id}` | `DELETE` | 退课 | N/A | `{"code": 200, "message": "退课成功", "data": null}` |
| `/api/enrollments/course/{courseId}/student/{studentId}` | `DELETE` | 根据课程和学生退课 | N/A | `{"code": 200, "message": "退课成功", "data": null}` |

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

#### 编写自定义测试

可以基于现有的测试文档，根据具体业务需求编写新的测试用例：

```http
POST http://localhost:8080/api/[endpoint]
Content-Type: application/json

{"your": "request", "body": "here"}
```

## 注意事项

1. 所有API响应均采用统一格式：`{"code": 状态码, "message": "消息", "data": 数据}`
2. 自动生成字段（如id、创建时间）在创建请求中不需要提供
3. 更新操作支持部分字段更新
4. 系统对关键数据进行唯一性校验和业务规则验证

## License

MIT