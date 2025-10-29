-- 校园选课系统数据库脚本 v1.1.0
-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS campus_course_selection_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE campus_course_selection_system;

-- 1. 教师表 (instructors)
CREATE TABLE IF NOT EXISTS instructors (
    id VARCHAR(36) NOT NULL PRIMARY KEY COMMENT 'UUID唯一标识符',
    instructor_id VARCHAR(20) NOT NULL UNIQUE COMMENT '教师编号，以T开头后跟至少3位数字',
    name VARCHAR(50) NOT NULL COMMENT '教师姓名',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '教师邮箱',
    department VARCHAR(100) NOT NULL COMMENT '所属院系',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. 学生表 (students)
CREATE TABLE IF NOT EXISTS students (
    id VARCHAR(36) NOT NULL PRIMARY KEY COMMENT 'UUID唯一标识符',
    student_id VARCHAR(20) NOT NULL UNIQUE COMMENT '学号',
    name VARCHAR(50) NOT NULL COMMENT '学生姓名',
    major VARCHAR(100) NOT NULL COMMENT '专业名称',
    grade INT NOT NULL COMMENT '入学年份',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱地址',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT chk_grade_range CHECK (grade >= 2000 AND grade <= 2100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. 课程时间安排表 (schedule_slots)
CREATE TABLE IF NOT EXISTS schedule_slots (
    id VARCHAR(36) NOT NULL PRIMARY KEY COMMENT 'UUID唯一标识符',
    day_of_week VARCHAR(10) NOT NULL COMMENT '上课星期，取值范围：MONDAY~SUNDAY',
    start_time TIME NOT NULL COMMENT '开始时间',
    end_time TIME NOT NULL COMMENT '结束时间',
    expected_attendance INT COMMENT '预计出勤人数',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT chk_day_of_week CHECK (day_of_week IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY')),
    CONSTRAINT chk_expected_attendance CHECK (expected_attendance >= 0 AND expected_attendance <= 1000)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. 课程表 (courses)
CREATE TABLE IF NOT EXISTS courses (
    id VARCHAR(36) NOT NULL PRIMARY KEY COMMENT 'UUID唯一标识符',
    code VARCHAR(20) NOT NULL UNIQUE COMMENT '课程编号',
    title VARCHAR(100) NOT NULL COMMENT '课程标题',
    instructor_id VARCHAR(36) NOT NULL COMMENT '授课教师ID，关联instructors表',
    schedule_id VARCHAR(36) NOT NULL COMMENT '时间表ID，关联schedule_slots表',
    capacity INT NOT NULL COMMENT '课程最大可选人数',
    enrolled INT NOT NULL DEFAULT 0 COMMENT '当前选课人数',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT chk_capacity CHECK (capacity >= 1 AND capacity <= 500),
    CONSTRAINT chk_enrolled CHECK (enrolled >= 0 AND enrolled <= capacity),
    INDEX idx_instructor_id (instructor_id),
    INDEX idx_schedule_id (schedule_id)
    -- 注意：外键约束在实际部署时可能需要根据业务需求调整
    -- FOREIGN KEY (instructor_id) REFERENCES instructors(id) ON DELETE CASCADE,
    -- FOREIGN KEY (schedule_id) REFERENCES schedule_slots(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. 选课记录表 (enrollments)
CREATE TABLE IF NOT EXISTS enrollments (
    id VARCHAR(36) NOT NULL PRIMARY KEY COMMENT 'UUID唯一标识符',
    course_id VARCHAR(36) NOT NULL COMMENT '课程ID，关联courses表',
    student_id VARCHAR(36) NOT NULL COMMENT '学生ID，关联students表',
    enrolled_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    status VARCHAR(20) NOT NULL DEFAULT 'ENROLLED' COMMENT '选课状态：ENROLLED, WITHDRAWN, COMPLETED, FAILED',
    grade DECIMAL(5,2) COMMENT '成绩',
    CONSTRAINT uk_course_student UNIQUE (course_id, student_id),
    CONSTRAINT chk_status CHECK (status IN ('ENROLLED', 'WITHDRAWN', 'COMPLETED', 'FAILED')),
    CONSTRAINT chk_grade CHECK (grade >= 0.0 AND grade <= 100.0),
    INDEX idx_course_id (course_id),
    INDEX idx_student_id (student_id)
    -- 注意：外键约束在实际部署时可能需要根据业务需求调整
    -- FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    -- FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建视图：课程详情视图（包含教师信息和时间安排）
CREATE VIEW IF NOT EXISTS course_details AS
SELECT 
    c.id AS course_id,
    c.code,
    c.title,
    c.capacity,
    c.enrolled,
    c.created_at AS course_created_at,
    i.id AS instructor_id,
    i.instructor_id AS instructor_code,
    i.name AS instructor_name,
    i.department,
    s.day_of_week,
    s.start_time,
    s.end_time,
    s.expected_attendance
FROM 
    courses c
LEFT JOIN 
    instructors i ON c.instructor_id = i.id
LEFT JOIN 
    schedule_slots s ON c.schedule_id = s.id;

-- 创建视图：学生选课信息视图
CREATE VIEW IF NOT EXISTS student_enrollment_details AS
SELECT 
    e.id AS enrollment_id,
    e.course_id,
    e.student_id,
    e.enrolled_at,
    e.status,
    e.grade,
    c.code AS course_code,
    c.title AS course_title,
    i.name AS instructor_name,
    s.day_of_week,
    s.start_time,
    s.end_time
FROM 
    enrollments e
LEFT JOIN 
    courses c ON e.course_id = c.id
LEFT JOIN 
    instructors i ON c.instructor_id = i.id
LEFT JOIN 
    schedule_slots s ON c.schedule_id = s.id;

-- 插入示例数据
-- 1. 插入示例教师数据
INSERT INTO instructors (id, instructor_id, name, email, department)
VALUES 
    (UUID(), 'T001', '张教授', 'zhang@example.edu.cn', '计算机学院'),
    (UUID(), 'T002', '李副教授', 'li@example.edu.cn', '电子工程学院'),
    (UUID(), 'T003', '王讲师', 'wang@example.edu.cn', '数学学院');

-- 2. 插入示例课程时间安排
INSERT INTO schedule_slots (id, day_of_week, start_time, end_time, expected_attendance)
VALUES 
    (UUID(), 'MONDAY', '08:00:00', '10:00:00', 50),
    (UUID(), 'WEDNESDAY', '14:00:00', '16:00:00', 40),
    (UUID(), 'FRIDAY', '09:00:00', '11:00:00', 30);

-- 3. 插入示例学生数据
INSERT INTO students (id, student_id, name, major, grade, email)
VALUES 
    (UUID(), '2024001', '张三', '计算机科学与技术', 2024, 'zhangsan@example.com'),
    (UUID(), '2024002', '李四', '软件工程', 2024, 'lisi@example.com'),
    (UUID(), '2024003', '王五', '人工智能', 2024, 'wangwu@example.com');

-- 4. 插入示例课程数据（需要使用上面生成的UUID）
-- 注意：由于UUID是动态生成的，这里只提供插入格式，实际执行时需要先查询生成的UUID
-- INSERT INTO courses (id, code, title, instructor_id, schedule_id, capacity)
-- VALUES
--     (UUID(), 'CS101', '计算机科学导论', (SELECT id FROM instructors WHERE instructor_id = 'T001'), (SELECT id FROM schedule_slots LIMIT 1), 60);

-- 5. 插入示例选课数据（需要使用上面生成的UUID）
-- 注意：由于UUID是动态生成的，这里只提供插入格式
-- INSERT INTO enrollments (id, course_id, student_id, status)
-- VALUES
--     (UUID(), (SELECT id FROM courses WHERE code = 'CS101'), (SELECT id FROM students WHERE student_id = '2024001'), 'ENROLLED');

-- 授权语句（根据实际需求调整）
-- GRANT ALL PRIVILEGES ON campus_course_selection_system.* TO 'admin'@'localhost' IDENTIFIED BY 'password';
-- FLUSH PRIVILEGES;

-- 显示数据库创建成功信息
SELECT '校园选课系统数据库创建成功！' AS message;
