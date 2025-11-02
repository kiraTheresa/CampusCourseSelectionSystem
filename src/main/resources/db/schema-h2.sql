-- H2 数据库表结构定义脚本（开发环境使用）
-- 注意：此脚本仅用于参考，实际开发环境中 JPA 会自动创建表结构
-- 如果需要在 H2 中手动创建表，可以使用此脚本

-- 学生表
CREATE TABLE IF NOT EXISTS students (
    id BINARY(16) PRIMARY KEY,
    student_id VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    major VARCHAR(255) NOT NULL,
    grade INT NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_student_id ON students(student_id);
CREATE INDEX IF NOT EXISTS idx_email ON students(email);
CREATE INDEX IF NOT EXISTS idx_major ON students(major);
CREATE INDEX IF NOT EXISTS idx_grade ON students(grade);

-- 课程表
CREATE TABLE IF NOT EXISTS courses (
    id BINARY(16) PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    title VARCHAR(100) NOT NULL,
    instructor_id VARCHAR(255) NOT NULL,
    schedule_id VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    enrolled INT NOT NULL DEFAULT 0,
    description VARCHAR(1000),
    credits INT,
    location VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_code ON courses(code);
CREATE INDEX IF NOT EXISTS idx_instructor_id ON courses(instructor_id);
CREATE INDEX IF NOT EXISTS idx_schedule_id ON courses(schedule_id);
CREATE INDEX IF NOT EXISTS idx_title ON courses(title);

-- 选课记录表
CREATE TABLE IF NOT EXISTS enrollments (
    id BINARY(16) PRIMARY KEY,
    course_id VARCHAR(255) NOT NULL,
    student_id VARCHAR(255) NOT NULL,
    enrolled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ENROLLED',
    grade DECIMAL(5,2),
    CONSTRAINT uk_course_student UNIQUE (course_id, student_id)
);

CREATE INDEX IF NOT EXISTS idx_course_id ON enrollments(course_id);
CREATE INDEX IF NOT EXISTS idx_student_id ON enrollments(student_id);
CREATE INDEX IF NOT EXISTS idx_status ON enrollments(status);
CREATE INDEX IF NOT EXISTS idx_course_student ON enrollments(course_id, student_id);

-- 教师表（如果独立使用）
CREATE TABLE IF NOT EXISTS instructors (
    id BINARY(16) PRIMARY KEY,
    instructor_id VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    department VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 时间表（如果独立使用）
CREATE TABLE IF NOT EXISTS schedule_slots (
    id BINARY(16) PRIMARY KEY,
    day_of_week VARCHAR(10) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    expected_attendance INT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

