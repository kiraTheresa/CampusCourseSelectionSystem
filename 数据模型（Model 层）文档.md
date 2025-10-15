# 🎓 课程选课系统数据模型文档（Data Model Specification）

## 一、模型概述

本系统用于管理学生选课信息，包括学生、课程、教师、课程时间表以及选课关系。
所有实体均采用 UUID 作为系统级唯一标识符，以确保在分布式环境下的数据唯一性。
时间戳字段（如 `createdAt`、`enrolledAt`）由系统自动生成。

---

## 二、实体关系图（ER 模型简述）

```
Student ───< Enrollment >─── Course ───< ScheduleSlot
                  │                  │
                  └────────────> Instructor
```

* 一个 **Student** 可以选修多门 **Course**
* 一个 **Course** 由一名 **Instructor** 授课
* 一个 **Course** 在一个 **ScheduleSlot** 时间段上课
* **Enrollment** 是学生与课程的多对多关联表

---

## 三、实体与字段说明

---

### 🧑‍🎓 Student 实体（学生表）

| 字段名       | 类型            | 说明                     | 约束      |
| --------- | ------------- | ---------------------- | ------- |
| id        | String        | 系统自动生成的唯一标识符（UUID）     | 主键      |
| studentId | String        | 学号（如 “2024001”），必须全局唯一 | 唯一、必填   |
| name      | String        | 学生姓名                   | 必填      |
| major     | String        | 专业名称（如 “计算机科学与技术”）     | 必填      |
| grade     | Integer       | 入学年份（如 2024）           | 必填      |
| email     | String        | 邮箱地址，必须符合邮箱格式          | 必填、格式校验 |
| createdAt | LocalDateTime | 创建时间戳，由系统自动生成          | 自动生成    |

---

### 📘 Course 实体（课程表）

| 字段名          | 类型            | 说明                             | 约束    |
| ------------ | ------------- | ------------------------------ | ----- |
| id           | String        | 系统自动生成的唯一标识符（UUID）             | 主键    |
| code         | String        | 课程编号（如 “CS101”），全局唯一           | 唯一、必填 |
| title        | String        | 课程标题（如 “计算机科学导论”）              | 必填    |
| instructorId | String        | 授课教师的唯一标识符（关联 Instructor.id）   | 外键、必填 |
| scheduleId   | String        | 课程对应的时间表编号（关联 ScheduleSlot.id） | 外键、必填 |
| capacity     | Integer       | 最大可选人数（如 60）                   | 必填、>0 |
| createdAt    | LocalDateTime | 创建时间戳                          | 自动生成  |

---

### 👨‍🏫 Instructor 实体（教师表）

| 字段名          | 类型            | 说明                                                              | 约束      |
| ------------ | ------------- | --------------------------------------------------------------- | ------- |
| id           | String        | 系统自动生成的唯一标识符（UUID）                                              | 主键      |
| instructorId | String        | 教师编号（如 “T001”），全局唯一                                             | 唯一、必填   |
| name         | String        | 教师姓名（如 “张教授”）                                                   | 必填      |
| email        | String        | 教师邮箱地址（如 “[zhang@example.edu.cn](mailto:zhang@example.edu.cn)”） | 必填、格式校验 |
| department   | String        | 所属院系（如 “计算机学院”）                                                 | 必填      |
| createdAt    | LocalDateTime | 创建时间戳                                                           | 自动生成    |

---

### 🗓️ ScheduleSlot 实体（课程时间表）

| 字段名                | 类型            | 说明                                  | 约束   |
| ------------------ | ------------- | ----------------------------------- | ---- |
| id                 | String        | 系统自动生成的唯一标识符（UUID）                  | 主键   |
| dayOfWeek          | String        | 上课星期（如 “MONDAY”），取值范围：MONDAY–SUNDAY | 必填   |
| startTime          | LocalTime     | 上课开始时间（如 “08:00”）                   | 必填   |
| endTime            | LocalTime     | 下课结束时间（如 “10:00”）                   | 必填   |
| expectedAttendance | Integer       | 预计出勤人数（如 50）                        | 可选   |
| createdAt          | LocalDateTime | 创建时间戳                               | 自动生成 |

---

### 📝 Enrollment 实体（选课记录表）

| 字段名        | 类型            | 说明                             | 约束            |
| ---------- | ------------- | ------------------------------ | ------------- |
| id         | String        | 系统自动生成的唯一标识符（UUID）             | 主键            |
| courseId   | String        | 对应课程的唯一标识符（关联 Course.id）       | 外键、必填         |
| studentId  | String        | 对应学生的唯一标识符（关联 Student.id）      | 外键、必填         |
| enrolledAt | LocalDateTime | 选课时间戳，由系统自动生成                  | 自动生成          |
| status     | String        | 选课状态（ENROLLED=已选，WITHDRAWN=退选） | 默认 “ENROLLED” |
| grade      | Double        | 成绩（课程结束后录入，可为空）                | 可选            |

---

## 四、主外键与约束说明

| 约束类型                 | 说明                                                                                                                                                |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------- |
| 主键（PK）               | 每个实体的 `id` 字段均为主键，类型为 UUID                                                                                                                        |
| 唯一约束（Unique）         | `studentId`、`instructorId`、`code` 均需唯一                                                                                                            |
| 外键（FK）               | `Course.instructorId → Instructor.id`；`Course.scheduleId → ScheduleSlot.id`；`Enrollment.courseId → Course.id`；`Enrollment.studentId → Student.id` |
| 非空约束（Not Null）       | 所有核心业务字段（如 name、code、capacity）均要求非空                                                                                                               |
| 自动生成（Auto Generated） | `id`、`createdAt`、`enrolledAt` 等字段由系统自动生成                                                                                                          |

---

## 五、字段命名规范

| 分类   | 命名规则            | 示例                          |
| ---- | --------------- | --------------------------- |
| 主键字段 | 使用 `id`，全局 UUID | `id = "a1b2c3..."`          |
| 编号字段 | 使用 `<entity>Id` | `studentId`, `instructorId` |
| 时间字段 | 使用 `At` 结尾      | `createdAt`, `enrolledAt`   |
| 外键字段 | 使用对应实体名 + Id    | `courseId`, `scheduleId`    |

---

## 六、示例 JSON 数据

### 课程示例（Course）

```json
{
  "id": "uuid-001",
  "code": "CS101",
  "title": "计算机科学导论",
  "instructorId": "uuid-T001",
  "scheduleId": "uuid-S001",
  "capacity": 60,
  "createdAt": "2025-10-15T08:00:00"
}
```

### 教师示例（Instructor）

```json
{
  "id": "uuid-T001",
  "instructorId": "T001",
  "name": "张教授",
  "email": "zhang@example.edu.cn",
  "department": "计算机学院",
  "createdAt": "2025-10-10T09:00:00"
}
```

### 时间表示例（ScheduleSlot）

```json
{
  "id": "uuid-S001",
  "dayOfWeek": "MONDAY",
  "startTime": "08:00",
  "endTime": "10:00",
  "expectedAttendance": 50,
  "createdAt": "2025-10-10T09:10:00"
}
```

### 学生示例（Student）

```json
{
  "id": "uuid-S001",
  "studentId": "2024001",
  "name": "李同学",
  "major": "计算机科学与技术",
  "grade": 2024,
  "email": "li2024001@univ.edu.cn",
  "createdAt": "2025-10-10T09:30:00"
}
```

### 选课示例（Enrollment）

```json
{
  "id": "uuid-E001",
  "courseId": "uuid-001",
  "studentId": "uuid-S001",
  "enrolledAt": "2025-10-12T12:00:00",
  "status": "ENROLLED",
  "grade": null
}
```

---

## 七、扩展设计建议

* 可在 `Course` 中增加 `credit`（学分）与 `semester`（学期）字段
* 可在 `Enrollment` 中增加成绩状态（如 “PENDING”、“PASSED”、“FAILED”）
* 可在 `ScheduleSlot` 中引入 `location`（上课教室）字段
* 后续支持一门课程多个时间段时，应通过中间表 `Course_ScheduleSlot` 处理多对多关系

---
