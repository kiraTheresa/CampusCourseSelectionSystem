-- 数据库初始化数据脚本
-- 注意：使用 H2 数据库时，此脚本会自动执行（开发环境）
-- 使用 MySQL 时，可以手动执行此脚本插入测试数据

-- 注意：由于使用了 UUID，此处仅提供参考示例
-- 实际数据会在应用启动时通过 JPA 自动生成

-- 示例：插入教师数据（可选，如果使用独立的 Instructor 实体）
-- INSERT INTO instructors (id, instructor_id, name, email, department, created_at)
-- VALUES 
--     (UNHEX(REPLACE(UUID(), '-', '')), 'T001', '张教授', 'zhang@example.edu.cn', '计算机学院', NOW()),
--     (UNHEX(REPLACE(UUID(), '-', '')), 'T002', '李教授', 'li@example.edu.cn', '数学学院', NOW());

-- 示例：插入时间表数据（可选，如果使用独立的 ScheduleSlot 实体）
-- INSERT INTO schedule_slots (id, day_of_week, start_time, end_time, expected_attendance, created_at)
-- VALUES 
--     (UNHEX(REPLACE(UUID(), '-', '')), 'MONDAY', '08:00:00', '10:00:00', 50, NOW()),
--     (UNHEX(REPLACE(UUID(), '-', '')), 'WEDNESDAY', '14:00:00', '16:00:00', 50, NOW());

-- 说明：
-- 1. 开发环境（H2）会自动执行此脚本
-- 2. 生产环境（MySQL）建议通过应用程序的初始化逻辑或迁移工具（如 Flyway、Liquibase）来管理数据
-- 3. 此文件主要用于开发环境的测试数据初始化
-- 4. 由于使用了 JPA 和 ddl-auto=update，表结构会自动创建，无需手动执行 schema.sql

