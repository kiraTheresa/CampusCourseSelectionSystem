-- 数据库表结构定义脚本
-- 注意：使用 H2 数据库时，此脚本会自动执行（开发环境）
-- 使用 MySQL 时，可以手动执行此脚本创建表结构

-- 注意：由于使用了 JPA 的 ddl-auto=update，此脚本主要用于参考和手动初始化

-- 学生表
CREATE TABLE IF NOT EXISTS students (
    id BINARY(16) PRIMARY KEY,
    student_id VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    major VARCHAR(255) NOT NULL,
    grade INT NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_student_id (student_id),
    INDEX idx_email (email),
    INDEX idx_major (major),
    INDEX idx_grade (grade)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 课程表
CREATE TABLE IF NOT EXISTS courses (
    id BINARY(16) PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    title VARCHAR(100) NOT NULL,
    instructor_id VARCHAR(255) NOT NULL,
    schedule_id VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    enrolled INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_code (code),
    INDEX idx_instructor_id (instructor_id),
    INDEX idx_schedule_id (schedule_id),
    INDEX idx_title (title)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 选课记录表
CREATE TABLE IF NOT EXISTS enrollments (
    id BINARY(16) PRIMARY KEY,
    course_id VARCHAR(255) NOT NULL,
    student_id VARCHAR(255) NOT NULL,
    enrolled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ENROLLED',
    grade DECIMAL(5,2),
    UNIQUE KEY uk_course_student (course_id, student_id),
    INDEX idx_course_id (course_id),
    INDEX idx_student_id (student_id),
    INDEX idx_status (status),
    INDEX idx_course_student (course_id, student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 教师表（如果独立使用）
CREATE TABLE IF NOT EXISTS instructors (
    id BINARY(16) PRIMARY KEY,
    instructor_id VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    department VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 时间表（如果独立使用）
CREATE TABLE IF NOT EXISTS schedule_slots (
    id BINARY(16) PRIMARY KEY,
    day_of_week VARCHAR(10) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    expected_attendance INT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

