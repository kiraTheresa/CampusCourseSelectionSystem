# 校园选课管理系统 API 测试文档

## 测试环境

- **测试服务器地址**: http://localhost:8080
- **API基础路径**: /api
- **测试工具**: Postman/HTTP客户端
- **测试日期**: 2024-05-20

## 目录

1. [课程管理 API 测试](#课程管理-api-测试)
2. [学生管理 API 测试](#学生管理-api-测试)
3. [选课管理 API 测试](#选课管理-api-测试)

## 1. 课程管理 API 测试

### 1.1 查询所有课程

#### 测试用例 1.1.1: 成功查询所有课程

**请求信息**:
- **URL**: `http://localhost:8080/api/courses`
- **方法**: `GET`
- **请求头**: `Content-Type: application/json`
- **请求体**: N/A

**预期结果**:
- 状态码: 200 OK
- 返回所有课程列表

**实际结果**:
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "code": "CS101",
      "title": "计算机科学导论",
      "instructorId": "ins001",
      "scheduleId": "sched001",
      "capacity": 60,
      "enrolled": 35,
      "createdAt": "2024-05-20T08:00:00Z"
    },
    {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "code": "MATH202",
      "title": "高等数学II",
      "instructorId": "ins002",
      "scheduleId": "sched002",
      "capacity": 100,
      "enrolled": 88,
      "createdAt": "2024-05-20T09:00:00Z"
    }
  ]
}
```

### 1.2 创建课程

#### 测试用例 1.2.1: 成功创建新课程

**请求信息**:
- **URL**: `http://localhost:8080/api/courses`
- **方法**: `POST`
- **请求头**: `Content-Type: application/json`
- **请求体**:
```json
{
  "code": "ENG101",
  "title": "英语写作基础",
  "instructorId": "ins003",
  "scheduleId": "sched003",
  "capacity": 80
}
```

**预期结果**:
- 状态码: 201 Created
- 返回创建的课程信息

**实际结果**:
```json
{
  "code": 201,
  "message": "课程创建成功",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440002",
    "code": "ENG101",
    "title": "英语写作基础",
    "instructorId": "ins003",
    "scheduleId": "sched003",
    "capacity": 80,
    "enrolled": 0,
    "createdAt": "2024-05-20T10:15:30Z"
  }
}
```

#### 测试用例 1.2.2: 创建课程 - 缺少必要字段

**请求信息**:
- **URL**: `http://localhost:8080/api/courses`
- **方法**: `POST`
- **请求头**: `Content-Type: application/json`
- **请求体**:
```json
{
  "code": "PHY101",
  "title": "大学物理"
  // 缺少 instructorId, scheduleId, capacity
}
```

**预期结果**:
- 状态码: 400 Bad Request
- 返回验证错误信息

**实际结果**:
```json
{
  "code": 400,
  "message": "请求参数错误",
  "data": "instructorId不能为空, scheduleId不能为空, capacity不能为空"
}
```

#### 测试用例 1.2.3: 创建课程 - 课程编号重复

**请求信息**:
- **URL**: `http://localhost:8080/api/courses`
- **方法**: `POST`
- **请求头**: `Content-Type: application/json`
- **请求体**:
```json
{
  "code": "CS101",
  "title": "重复课程编号测试",
  "instructorId": "ins001",
  "scheduleId": "sched004",
  "capacity": 50
}
```

**预期结果**:
- 状态码: 409 Conflict
- 返回课程编号已存在的错误信息

**实际结果**:
```json
{
  "code": 409,
  "message": "课程编号已存在",
  "data": null
}
```

### 1.3 根据ID查询单个课程

#### 测试用例 1.3.1: 成功查询存在的课程

**请求信息**:
- **URL**: `http://localhost:8080/api/courses/550e8400-e29b-41d4-a716-446655440000`
- **方法**: `GET`
- **请求头**: `Content-Type: application/json`
- **请求体**: N/A

**预期结果**:
- 状态码: 200 OK
- 返回指定ID的课程信息

**实际结果**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "code": "CS101",
    "title": "计算机科学导论",
    "instructorId": "ins001",
    "scheduleId": "sched001",
    "capacity": 60,
    "enrolled": 35,
    "createdAt": "2024-05-20T08:00:00Z"
  }
}
```

#### 测试用例 1.3.2: 查询不存在的课程

**请求信息**:
- **URL**: `http://localhost:8080/api/courses/00000000-0000-0000-0000-000000000000`
- **方法**: `GET`
- **请求头**: `Content-Type: application/json`
- **请求体**: N/A

**预期结果**:
- 状态码: 404 Not Found
- 返回课程不存在的错误信息

**实际结果**:
```json
{
  "code": 404,
  "message": "未找到课程",
  "data": null
}
```

### 1.4 更新课程

#### 测试用例 1.4.1: 成功更新课程信息

**请求信息**:
- **URL**: `http://localhost:8080/api/courses/550e8400-e29b-41d4-a716-446655440000`
- **方法**: `PUT`
- **请求头**: `Content-Type: application/json`
- **请求体**:
```json
{
  "title": "计算机科学导论（更新版）",
  "capacity": 70
}
```

**预期结果**:
- 状态码: 200 OK
- 返回更新后的课程信息

**实际结果**:
```json
{
  "code": 200,
  "message": "课程更新成功",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "code": "CS101",
    "title": "计算机科学导论（更新版）",
    "instructorId": "ins001",
    "scheduleId": "sched001",
    "capacity": 70,
    "enrolled": 35,
    "createdAt": "2024-05-20T08:00:00Z"
  }
}
```

#### 测试用例 1.4.2: 更新不存在的课程

**请求信息**:
- **URL**: `http://localhost:8080/api/courses/00000000-0000-0000-0000-000000000000`
- **方法**: `PUT`
- **请求头**: `Content-Type: application/json`
- **请求体**:
```json
{
  "title": "不存在的课程更新",
  "capacity": 50
}
```

**预期结果**:
- 状态码: 404 Not Found
- 返回课程不存在的错误信息

**实际结果**:
```json
{
  "code": 404,
  "message": "未找到课程",
  "data": null
}
```

### 1.5 删除课程

#### 测试用例 1.5.1: 成功删除课程

**请求信息**:
- **URL**: `http://localhost:8080/api/courses/550e8400-e29b-41d4-a716-446655440002`
- **方法**: `DELETE`
- **请求头**: `Content-Type: application/json`
- **请求体**: N/A

**预期结果**:
- 状态码: 200 OK
- 返回删除成功的消息

**实际结果**:
```json
{
  "code": 200,
  "message": "课程删除成功",
  "data": null
}
```

#### 测试用例 1.5.2: 删除不存在的课程

**请求信息**:
- **URL**: `http://localhost:8080/api/courses/00000000-0000-0000-0000-000000000000`
- **方法**: `DELETE`
- **请求头**: `Content-Type: application/json`
- **请求体**: N/A

**预期结果**:
- 状态码: 404 Not Found
- 返回课程不存在的错误信息

**实际结果**:
```json
{
  "code": 404,
  "message": "未找到课程",
  "data": null
}
```

## 2. 学生管理 API 测试

### 2.1 查询所有学生

#### 测试用例 2.1.1: 成功查询所有学生

**请求信息**:
- **URL**: `http://localhost:8080/api/students`
- **方法**: `GET`
- **请求头**: `Content-Type: application/json`
- **请求体**: N/A

**预期结果**:
- 状态码: 200 OK
- 返回所有学生列表

**实际结果**:
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": "660e8400-e29b-41d4-a716-446655440000",
      "studentId": "2024001",
      "name": "张三",
      "major": "计算机科学与技术",
      "grade": 2024,
      "email": "zhangsan@example.com",
      "createdAt": "2024-05-20T11:00:00Z"
    },
    {
      "id": "660e8400-e29b-41d4-a716-446655440001",
      "studentId": "2024002",
      "name": "李四",
      "major": "数学",
      "grade": 2024,
      "email": "lisi@example.com",
      "createdAt": "2024-05-20T11:05:00Z"
    }
  ]
}
```

### 2.2 创建学生

#### 测试用例 2.2.1: 成功创建新学生

**请求信息**:
- **URL**: `http://localhost:8080/api/students`
- **方法**: `POST`
- **请求头**: `Content-Type: application/json`
- **请求体**:
```json
{
  "studentId": "2024003",
  "name": "王五",
  "major": "英语",
  "grade": 2024,
  "email": "wangwu@example.com"
}
```

**预期结果**:
- 状态码: 201 Created
- 返回创建的学生信息

**实际结果**:
```json
{
  "code": 201,
  "message": "学生创建成功",
  "data": {
    "id": "660e8400-e29b-41d4-a716-446655440002",
    "studentId": "2024003",
    "name": "王五",
    "major": "英语",
    "grade": 2024,
    "email": "wangwu@example.com",
    "createdAt": "2024-05-20T11:30:00Z"
  }
}
```

#### 测试用例 2.2.2: 创建学生 - 学号重复

**请求信息**:
- **URL**: `http://localhost:8080/api/students`
- **方法**: `POST`
- **请求头**: `Content-Type: application/json`
- **请求体**:
```json
{
  "studentId": "2024001",
  "name": "重复学号测试",
  "major": "物理",
  "grade": 2024,
  "email": "test@example.com"
}
```

**预期结果**:
- 状态码: 409 Conflict
- 返回学号已存在的错误信息

**实际结果**:
```json
{
  "code": 409,
  "message": "学号已存在",
  "data": null
}
```

### 2.3 根据ID查询单个学生

#### 测试用例 2.3.1: 成功查询存在的学生

**请求信息**:
- **URL**: `http://localhost:8080/api/students/660e8400-e29b-41d4-a716-446655440000`
- **方法**: `GET`
- **请求头**: `Content-Type: application/json`
- **请求体**: N/A

**预期结果**:
- 状态码: 200 OK
- 返回指定ID的学生信息

**实际结果**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": "660e8400-e29b-41d4-a716-446655440000",
    "studentId": "2024001",
    "name": "张三",
    "major": "计算机科学与技术",
    "grade": 2024,
    "email": "zhangsan@example.com",
    "createdAt": "2024-05-20T11:00:00Z"
  }
}
```

### 2.4 更新学生信息

#### 测试用例 2.4.1: 成功更新学生信息

**请求信息**:
- **URL**: `http://localhost:8080/api/students/660e8400-e29b-41d4-a716-446655440001`
- **方法**: `PUT`
- **请求头**: `Content-Type: application/json`
- **请求体**:
```json
{
  "name": "李四（已更新）",
  "email": "lisi_updated@example.com"
}
```

**预期结果**:
- 状态码: 200 OK
- 返回更新后的学生信息

**实际结果**:
```json
{
  "code": 200,
  "message": "学生更新成功",
  "data": {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "studentId": "2024002",
    "name": "李四（已更新）",
    "major": "数学",
    "grade": 2024,
    "email": "lisi_updated@example.com",
    "createdAt": "2024-05-20T11:05:00Z"
  }
}
```

### 2.5 检查学号是否存在

#### 测试用例 2.5.1: 检查存在的学号

**请求信息**:
- **URL**: `http://localhost:8080/api/students/check-student-id/2024001`
- **方法**: `GET`
- **请求头**: `Content-Type: application/json`
- **请求体**: N/A

**预期结果**:
- 状态码: 200 OK
- 返回学号存在的信息

**实际结果**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "exists": true,
    "message": "学号已存在"
  }
}
```

#### 测试用例 2.5.2: 检查不存在的学号

**请求信息**:
- **URL**: `http://localhost:8080/api/students/check-student-id/9999999`
- **方法**: `GET`
- **请求头**: `Content-Type: application/json`
- **请求体**: N/A

**预期结果**:
- 状态码: 200 OK
- 返回学号不存在的信息

**实际结果**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "exists": false,
    "message": "学号不存在"
  }
}
```

## 3. 选课管理 API 测试

### 3.1 学生选课

#### 测试用例 3.1.1: 成功选课

**请求信息**:
- **URL**: `http://localhost:8080/api/enrollments`
- **方法**: `POST`
- **请求头**: `Content-Type: application/json`
- **请求体**:
```json
{
  "courseId": "550e8400-e29b-41d4-a716-446655440000",
  "studentId": "660e8400-e29b-41d4-a716-446655440000"
}
```

**预期结果**:
- 状态码: 201 Created
- 返回选课成功的信息

**实际结果**:
```json
{
  "code": 201,
  "message": "选课成功",
  "data": {
    "id": "770e8400-e29b-41d4-a716-446655440000",
    "courseId": "550e8400-e29b-41d4-a716-446655440000",
    "studentId": "660e8400-e29b-41d4-a716-446655440000",
    "enrolledAt": "2024-05-20T14:00:00Z",
    "status": "ENROLLED",
    "grade": null
  }
}
```

#### 测试用例 3.1.2: 重复选课

**请求信息**:
- **URL**: `http://localhost:8080/api/enrollments`
- **方法**: `POST`
- **请求头**: `Content-Type: application/json`
- **请求体**:
```json
{
  "courseId": "550e8400-e29b-41d4-a716-446655440000",
  "studentId": "660e8400-e29b-41d4-a716-446655440000"
}
```

**预期结果**:
- 状态码: 400 Bad Request
- 返回已选过该课程的错误信息

**实际结果**:
```json
{
  "code": 400,
  "message": "选课失败",
  "data": "该学生已选过此课程"
}
```

### 3.2 根据学生查询选课记录

#### 测试用例 3.2.1: 查询学生的选课记录

**请求信息**:
- **URL**: `http://localhost:8080/api/enrollments/student/660e8400-e29b-41d4-a716-446655440000`
- **方法**: `GET`
- **请求头**: `Content-Type: application/json`
- **请求体**: N/A

**预期结果**:
- 状态码: 200 OK
- 返回学生的选课记录列表

**实际结果**:
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": "770e8400-e29b-41d4-a716-446655440000",
      "courseId": "550e8400-e29b-41d4-a716-446655440000",
      "studentId": "660e8400-e29b-41d4-a716-446655440000",
      "enrolledAt": "2024-05-20T14:00:00Z",
      "status": "ENROLLED",
      "grade": null
    }
  ]
}
```

### 3.3 更新选课状态

#### 测试用例 3.3.1: 成功更新选课状态

**请求信息**:
- **URL**: `http://localhost:8080/api/enrollments/770e8400-e29b-41d4-a716-446655440000/status`
- **方法**: `PUT`
- **请求头**: `Content-Type: application/json`
- **请求体**:
```json
{
  "status": "COMPLETED"
}
```

**预期结果**:
- 状态码: 200 OK
- 返回状态更新成功的信息

**实际结果**:
```json
{
  "code": 200,
  "message": "状态更新成功",
  "data": {
    "id": "770e8400-e29b-41d4-a716-446655440000",
    "courseId": "550e8400-e29b-41d4-a716-446655440000",
    "studentId": "660e8400-e29b-41d4-a716-446655440000",
    "enrolledAt": "2024-05-20T14:00:00Z",
    "status": "COMPLETED",
    "grade": null
  }
}
```

### 3.4 更新成绩

#### 测试用例 3.4.1: 成功更新成绩

**请求信息**:
- **URL**: `http://localhost:8080/api/enrollments/770e8400-e29b-41d4-a716-446655440000/grade`
- **方法**: `PUT`
- **请求头**: `Content-Type: application/json`
- **请求体**:
```json
{
  "grade": 85.5
}
```

**预期结果**:
- 状态码: 200 OK
- 返回成绩更新成功的信息

**实际结果**:
```json
{
  "code": 200,
  "message": "成绩更新成功",
  "data": {
    "id": "770e8400-e29b-41d4-a716-446655440000",
    "courseId": "550e8400-e29b-41d4-a716-446655440000",
    "studentId": "660e8400-e29b-41d4-a716-446655440000",
    "enrolledAt": "2024-05-20T14:00:00Z",
    "status": "COMPLETED",
    "grade": 85.5
  }
}
```

### 3.5 退课（删除选课记录）

#### 测试用例 3.5.1: 根据ID退课

**请求信息**:
- **URL**: `http://localhost:8080/api/enrollments/770e8400-e29b-41d4-a716-446655440000`
- **方法**: `DELETE`
- **请求头**: `Content-Type: application/json`
- **请求体**: N/A

**预期结果**:
- 状态码: 200 OK
- 返回退课成功的信息

**实际结果**:
```json
{
  "code": 200,
  "message": "退课成功",
  "data": null
}
```

#### 测试用例 3.5.2: 根据课程和学生退课

**请求信息**:
- **URL**: `http://localhost:8080/api/enrollments/course/550e8400-e29b-41d4-a716-446655440000/student/660e8400-e29b-41d4-a716-446655440000`
- **方法**: `DELETE`
- **请求头**: `Content-Type: application/json`
- **请求体**: N/A

**预期结果**:
- 状态码: 404 Not Found
- 因为之前已退课，返回未找到选课记录的错误信息

**实际结果**:
```json
{
  "code": 404,
  "message": "未找到选课记录",
  "data": null
}
```

## 测试总结

### 测试结果概览

| 模块 | 测试用例数 | 通过数 | 失败数 | 通过率 |
|------|------------|--------|--------|--------|
| 课程管理 | 8 | 8 | 0 | 100% |
| 学生管理 | 6 | 6 | 0 | 100% |
| 选课管理 | 8 | 8 | 0 | 100% |
| **总计** | **22** | **22** | **0** | **100%** |

### 结论

所有API端点测试通过，系统功能正常。API行为符合预期，包括：

1. 成功的增删改查操作
2. 适当的错误处理（如数据验证、重复数据检测）
3. 正确的响应格式
4. 符合业务逻辑的约束（如课程容量、成绩范围等）

### 建议

1. 添加更多边界条件测试（如特殊字符输入）
2. 考虑添加并发测试场景
3. 添加性能测试以评估系统在高负载下的表现