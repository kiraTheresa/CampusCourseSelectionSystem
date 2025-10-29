package com.zjgsu.ms.hxy.CampusCourseSelectionSystem.repository;

import com.zjgsu.ms.hxy.CampusCourseSelectionSystem.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * EnrollmentRepository 接口
 * 使用 Spring Data JPA 实现数据库持久化，提供选课记录的 CRUD 操作
 *
 * @author System
 * @version 1.1.0
 * @since 2024
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {



    /**
     * 根据课程ID查找选课记录
     * @param courseId 课程ID
     * @return 该课程的所有选课记录列表
     */
    List<Enrollment> findByCourseId(String courseId);

    /**
     * 根据学生ID查找选课记录
     * @param studentId 学生ID
     * @return 该学生的所有选课记录列表
     */
    List<Enrollment> findByStudentId(String studentId);

    /**
     * 根据课程ID和学生ID查找选课记录
     * @param courseId 课程ID
     * @param studentId 学生ID
     * @return 包含选课记录的Optional，如果不存在则返回空Optional
     */
    Optional<Enrollment> findByCourseIdAndStudentId(String courseId, String studentId);

    /**
     * 根据状态查找选课记录
     * @param status 选课状态
     * @return 指定状态的所有选课记录列表
     */
    List<Enrollment> findByStatus(String status);

    /**
     * 根据课程ID和学生ID删除选课记录（退课）
     * @param courseId 课程ID
     * @param studentId 学生ID
     * @return 如果选课记录存在并被删除返回true，否则返回false
     */
    boolean deleteByCourseIdAndStudentId(String courseId, String studentId);

    /**
     * 检查学生是否已选某课程（排除已退课的情况）
     * @param courseId 课程ID
     * @param studentId 学生ID
     * @return 如果选课记录存在返回true，否则返回false
     */
    boolean existsByCourseIdAndStudentIdAndStatusNot(String courseId, String studentId, String status);

    /**
     * 获取课程的选课人数（排除已退课的情况）
     * @param courseId 课程ID
     * @param status 排除的状态
     * @return 该课程的选课人数
     */
    long countByCourseIdAndStatusNot(String courseId, String status);

    /**
     * 获取学生的选课数量（排除已退课的情况）
     * @param studentId 学生ID
     * @param status 排除的状态
     * @return 该学生的选课数量
     */
    long countByStudentIdAndStatusNot(String studentId, String status);
}