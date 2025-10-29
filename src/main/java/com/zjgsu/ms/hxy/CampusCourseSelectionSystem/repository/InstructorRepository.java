package com.zjgsu.ms.hxy.CampusCourseSelectionSystem.repository;

import com.zjgsu.ms.hxy.CampusCourseSelectionSystem.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * InstructorRepository 接口
 * 使用 Spring Data JPA 实现数据库持久化，提供教师的 CRUD 操作
 *
 * @author System
 * @version 1.1.0
 * @since 2024
 */
@Repository
public interface InstructorRepository extends JpaRepository<Instructor, UUID> {

    /**
     * 根据教师ID查找教师
     * @param instructorId 教师ID
     * @return 包含教师的Optional，如果不存在则返回空Optional
     */
    Optional<Instructor> findByInstructorId(String instructorId);

    /**
     * 根据邮箱查找教师
     * @param email 邮箱地址
     * @return 包含教师的Optional，如果不存在则返回空Optional
     */
    Optional<Instructor> findByEmail(String email);

    /**
     * 检查教师ID是否存在
     * @param instructorId 教师ID
     * @return 如果教师ID存在返回true，否则返回false
     */
    boolean existsByInstructorId(String instructorId);

    /**
     * 检查邮箱是否存在
     * @param email 邮箱地址
     * @return 如果邮箱存在返回true，否则返回false
     */
    boolean existsByEmail(String email);

    /**
     * 根据部门查找教师
     * @param department 部门名称
     * @return 该部门的所有教师列表
     */
    List<Instructor> findByDepartment(String department);

    /**
     * 根据姓名关键词搜索教师
     * @param keyword 关键词
     * @return 匹配的教师列表
     */
    List<Instructor> findByNameContaining(String keyword);
}