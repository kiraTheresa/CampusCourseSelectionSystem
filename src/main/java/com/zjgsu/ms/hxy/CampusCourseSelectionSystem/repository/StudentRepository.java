package com.zjgsu.ms.hxy.CampusCourseSelectionSystem.repository;

import com.zjgsu.ms.hxy.CampusCourseSelectionSystem.model.Student;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * StudentRepository 类
 * 使用 ConcurrentHashMap 模拟数据库，提供学生的基本 CRUD 操作
 *
 * @author System
 * @version 1.0
 * @since 2024
 */
@Repository
public class StudentRepository {
    private final Map<UUID, Student> students = new ConcurrentHashMap<>();

    /**
     * 查找所有学生
     * @return 学生列表
     */
    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }

    /**
     * 根据ID查找学生
     * @param id 学生ID
     * @return 包含学生的Optional，如果不存在则返回空Optional
     */
    public Optional<Student> findById(UUID id) {
        return Optional.ofNullable(students.get(id));
    }

    /**
     * 根据学号查找学生
     * @param studentId 学号
     * @return 包含学生的Optional，如果不存在则返回空Optional
     */
    public Optional<Student> findByStudentId(String studentId) {
        return students.values().stream()
                .filter(student -> student.getStudentId().equals(studentId))
                .findFirst();
    }

    /**
     * 根据邮箱查找学生
     * @param email 邮箱地址
     * @return 包含学生的Optional，如果不存在则返回空Optional
     */
    public Optional<Student> findByEmail(String email) {
        return students.values().stream()
                .filter(student -> student.getEmail().equals(email))
                .findFirst();
    }

    /**
     * 保存学生（新增或更新）
     * @param student 要保存的学生
     * @return 保存后的学生
     */
    public Student save(Student student) {
        if (student.getId() == null) {
            // 新增学生，生成UUID
            student.setId(UUID.randomUUID());
        }
        students.put(student.getId(), student);
        return student;
    }

    /**
     * 批量保存学生
     * @param studentList 学生列表
     * @return 保存后的学生列表
     */
    public List<Student> saveAll(List<Student> studentList) {
        studentList.forEach(this::save);
        return studentList;
    }

    /**
     * 根据ID删除学生
     * @param id 学生ID
     * @return 如果学生存在并被删除返回true，否则返回false
     */
    public boolean deleteById(UUID id) {
        return students.remove(id) != null;
    }

    /**
     * 根据学号删除学生
     * @param studentId 学号
     * @return 如果学生存在并被删除返回true，否则返回false
     */
    public boolean deleteByStudentId(String studentId) {
        Optional<Student> student = findByStudentId(studentId);
        if (student.isPresent()) {
            students.remove(student.get().getId());
            return true;
        }
        return false;
    }

    /**
     * 删除所有学生
     */
    public void deleteAll() {
        students.clear();
    }

    /**
     * 检查学生是否存在
     * @param id 学生ID
     * @return 如果学生存在返回true，否则返回false
     */
    public boolean existsById(UUID id) {
        return students.containsKey(id);
    }

    /**
     * 检查学号是否存在
     * @param studentId 学号
     * @return 如果学号存在返回true，否则返回false
     */
    public boolean existsByStudentId(String studentId) {
        return students.values().stream()
                .anyMatch(student -> student.getStudentId().equals(studentId));
    }

    /**
     * 检查邮箱是否存在
     * @param email 邮箱地址
     * @return 如果邮箱存在返回true，否则返回false
     */
    public boolean existsByEmail(String email) {
        return students.values().stream()
                .anyMatch(student -> student.getEmail().equals(email));
    }

    /**
     * 获取学生数量
     * @return 学生总数
     */
    public long count() {
        return students.size();
    }
}