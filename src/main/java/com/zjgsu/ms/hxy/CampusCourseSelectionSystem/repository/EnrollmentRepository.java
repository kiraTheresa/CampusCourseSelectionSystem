package com.zjgsu.ms.hxy.CampusCourseSelectionSystem.repository;

import com.zjgsu.ms.hxy.CampusCourseSelectionSystem.model.Enrollment;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * EnrollmentRepository 类
 * 使用 ConcurrentHashMap 模拟数据库，提供选课记录的 CRUD 操作
 *
 * @author System
 * @version 1.0
 * @since 2024
 */
@Repository
public class EnrollmentRepository {
    private final Map<UUID, Enrollment> enrollments = new ConcurrentHashMap<>();

    /**
     * 查找所有选课记录
     * @return 选课记录列表
     */
    public List<Enrollment> findAll() {
        return new ArrayList<>(enrollments.values());
    }

    /**
     * 根据ID查找选课记录
     * @param id 选课记录ID
     * @return 包含选课记录的Optional，如果不存在则返回空Optional
     */
    public Optional<Enrollment> findById(UUID id) {
        return Optional.ofNullable(enrollments.get(id));
    }

    /**
     * 根据课程ID查找选课记录
     * @param courseId 课程ID
     * @return 该课程的所有选课记录列表
     */
    public List<Enrollment> findByCourseId(String courseId) {
        return enrollments.values().stream()
                .filter(enrollment -> enrollment.getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }

    /**
     * 根据学生ID查找选课记录
     * @param studentId 学生ID
     * @return 该学生的所有选课记录列表
     */
    public List<Enrollment> findByStudentId(String studentId) {
        return enrollments.values().stream()
                .filter(enrollment -> enrollment.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    /**
     * 根据课程ID和学生ID查找选课记录
     * @param courseId 课程ID
     * @param studentId 学生ID
     * @return 包含选课记录的Optional，如果不存在则返回空Optional
     */
    public Optional<Enrollment> findByCourseIdAndStudentId(String courseId, String studentId) {
        return enrollments.values().stream()
                .filter(enrollment -> enrollment.getCourseId().equals(courseId)
                        && enrollment.getStudentId().equals(studentId))
                .findFirst();
    }

    /**
     * 根据状态查找选课记录
     * @param status 选课状态
     * @return 指定状态的所有选课记录列表
     */
    public List<Enrollment> findByStatus(String status) {
        return enrollments.values().stream()
                .filter(enrollment -> enrollment.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    /**
     * 保存选课记录（新增或更新）
     * @param enrollment 要保存的选课记录
     * @return 保存后的选课记录
     */
    public Enrollment save(Enrollment enrollment) {
        if (enrollment.getId() == null) {
            // 新增选课记录，生成UUID
            enrollment.setId(UUID.randomUUID());
        }
        enrollments.put(enrollment.getId(), enrollment);
        return enrollment;
    }

    /**
     * 批量保存选课记录
     * @param enrollmentList 选课记录列表
     * @return 保存后的选课记录列表
     */
    public List<Enrollment> saveAll(List<Enrollment> enrollmentList) {
        enrollmentList.forEach(this::save);
        return enrollmentList;
    }

    /**
     * 根据ID删除选课记录
     * @param id 选课记录ID
     * @return 如果选课记录存在并被删除返回true，否则返回false
     */
    public boolean deleteById(UUID id) {
        return enrollments.remove(id) != null;
    }

    /**
     * 根据课程ID和学生ID删除选课记录（退课）
     * @param courseId 课程ID
     * @param studentId 学生ID
     * @return 如果选课记录存在并被删除返回true，否则返回false
     */
    public boolean deleteByCourseIdAndStudentId(String courseId, String studentId) {
        Optional<Enrollment> enrollment = findByCourseIdAndStudentId(courseId, studentId);
        if (enrollment.isPresent()) {
            enrollments.remove(enrollment.get().getId());
            return true;
        }
        return false;
    }

    /**
     * 删除所有选课记录
     */
    public void deleteAll() {
        enrollments.clear();
    }

    /**
     * 检查选课记录是否存在
     * @param id 选课记录ID
     * @return 如果选课记录存在返回true，否则返回false
     */
    public boolean existsById(UUID id) {
        return enrollments.containsKey(id);
    }

    /**
     * 检查学生是否已选某课程
     * @param courseId 课程ID
     * @param studentId 学生ID
     * @return 如果选课记录存在返回true，否则返回false
     */
    public boolean existsByCourseIdAndStudentId(String courseId, String studentId) {
        return enrollments.values().stream()
                .anyMatch(enrollment -> enrollment.getCourseId().equals(courseId)
                        && enrollment.getStudentId().equals(studentId)
                        && !"WITHDRAWN".equals(enrollment.getStatus()));
    }

    /**
     * 获取选课记录数量
     * @return 选课记录总数
     */
    public long count() {
        return enrollments.size();
    }

    /**
     * 获取课程的选课人数
     * @param courseId 课程ID
     * @return 该课程的选课人数
     */
    public long countByCourseId(String courseId) {
        return enrollments.values().stream()
                .filter(enrollment -> enrollment.getCourseId().equals(courseId)
                        && !"WITHDRAWN".equals(enrollment.getStatus()))
                .count();
    }

    /**
     * 获取学生的选课数量
     * @param studentId 学生ID
     * @return 该学生的选课数量
     */
    public long countByStudentId(String studentId) {
        return enrollments.values().stream()
                .filter(enrollment -> enrollment.getStudentId().equals(studentId)
                        && !"WITHDRAWN".equals(enrollment.getStatus()))
                .count();
    }

    /**
     * 更新选课状态
     * @param id 选课记录ID
     * @param status 新状态
     * @return 更新后的选课记录Optional，如果不存在则返回空Optional
     */
    public Optional<Enrollment> updateStatus(UUID id, String status) {
        Enrollment enrollment = enrollments.get(id);
        if (enrollment != null) {
            enrollment.setStatus(status);
            enrollments.put(id, enrollment);
            return Optional.of(enrollment);
        }
        return Optional.empty();
    }

    /**
     * 更新成绩
     * @param id 选课记录ID
     * @param grade 成绩
     * @return 更新后的选课记录Optional，如果不存在则返回空Optional
     */
    public Optional<Enrollment> updateGrade(UUID id, Double grade) {
        Enrollment enrollment = enrollments.get(id);
        if (enrollment != null) {
            enrollment.setGrade(grade);
            enrollments.put(id, enrollment);
            return Optional.of(enrollment);
        }
        return Optional.empty();
    }

    /**
     * 批量更新课程成绩
     * @param courseId 课程ID
     * @param grades 学生ID到成绩的映射
     * @return 更新成功的数量
     */
    public int updateGradesForCourse(String courseId, Map<String, Double> grades) {
        int updatedCount = 0;
        for (Enrollment enrollment : enrollments.values()) {
            if (enrollment.getCourseId().equals(courseId) && grades.containsKey(enrollment.getStudentId())) {
                enrollment.setGrade(grades.get(enrollment.getStudentId()));
                updatedCount++;
            }
        }
        return updatedCount;
    }
}