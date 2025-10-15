package com.zjgsu.ms.hxy.CampusCourseSelectionSystem.repository;

import com.zjgsu.ms.hxy.CampusCourseSelectionSystem.model.Course;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CourseRepository 类
 * 使用 ConcurrentHashMap 模拟数据库，提供课程的 CRUD 操作
 *
 * @author System
 * @version 1.0
 * @since 2024
 */
@Repository
public class CourseRepository {
    private final Map<UUID, Course> courses = new ConcurrentHashMap<>();

    /**
     * 查找所有课程
     * @return 课程列表
     */
    public List<Course> findAll() {
        return new ArrayList<>(courses.values());
    }

    /**
     * 根据ID查找课程
     * @param id 课程ID
     * @return 包含课程的Optional，如果不存在则返回空Optional
     */
    public Optional<Course> findById(UUID id) {
        return Optional.ofNullable(courses.get(id));
    }

    /**
     * 根据课程编号查找课程
     * @param code 课程编号
     * @return 包含课程的Optional，如果不存在则返回空Optional
     */
    public Optional<Course> findByCode(String code) {
        return courses.values().stream()
                .filter(course -> course.getCode().equals(code))
                .findFirst();
    }

    /**
     * 根据教师ID查找课程
     * @param instructorId 教师ID
     * @return 该教师的所有课程列表
     */
    public List<Course> findByInstructorId(String instructorId) {
        return courses.values().stream()
                .filter(course -> course.getInstructorId().equals(instructorId))
                .toList();
    }

    /**
     * 根据时间表ID查找课程
     * @param scheduleId 时间表ID
     * @return 使用该时间表的课程列表
     */
    public List<Course> findByScheduleId(String scheduleId) {
        return courses.values().stream()
                .filter(course -> course.getScheduleId().equals(scheduleId))
                .toList();
    }

    /**
     * 保存课程（新增或更新）
     * @param course 要保存的课程
     * @return 保存后的课程
     */
    public Course save(Course course) {
        if (course.getId() == null) {
            // 新增课程，生成UUID
            course.setId(UUID.randomUUID());
        }
        courses.put(course.getId(), course);
        return course;
    }

    /**
     * 批量保存课程
     * @param courseList 课程列表
     * @return 保存后的课程列表
     */
    public List<Course> saveAll(List<Course> courseList) {
        courseList.forEach(this::save);
        return courseList;
    }

    /**
     * 根据ID删除课程
     * @param id 课程ID
     * @return 如果课程存在并被删除返回true，否则返回false
     */
    public boolean deleteById(UUID id) {
        return courses.remove(id) != null;
    }

    /**
     * 根据课程编号删除课程
     * @param code 课程编号
     * @return 如果课程存在并被删除返回true，否则返回false
     */
    public boolean deleteByCode(String code) {
        Optional<Course> course = findByCode(code);
        if (course.isPresent()) {
            courses.remove(course.get().getId());
            return true;
        }
        return false;
    }

    /**
     * 删除所有课程
     */
    public void deleteAll() {
        courses.clear();
    }

    /**
     * 检查课程是否存在
     * @param id 课程ID
     * @return 如果课程存在返回true，否则返回false
     */
    public boolean existsById(UUID id) {
        return courses.containsKey(id);
    }

    /**
     * 检查课程编号是否存在
     * @param code 课程编号
     * @return 如果课程编号存在返回true，否则返回false
     */
    public boolean existsByCode(String code) {
        return courses.values().stream()
                .anyMatch(course -> course.getCode().equals(code));
    }

    /**
     * 获取课程数量
     * @return 课程总数
     */
    public long count() {
        return courses.size();
    }

    /**
     * 根据课程标题关键词搜索课程
     * @param keyword 关键词
     * @return 匹配的课程列表
     */
    public List<Course> findByTitleContaining(String keyword) {
        return courses.values().stream()
                .filter(course -> course.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    /**
     * 根据容量范围查找课程
     * @param minCapacity 最小容量
     * @param maxCapacity 最大容量
     * @return 容量在指定范围内的课程列表
     */
    public List<Course> findByCapacityBetween(Integer minCapacity, Integer maxCapacity) {
        return courses.values().stream()
                .filter(course -> course.getCapacity() >= minCapacity && course.getCapacity() <= maxCapacity)
                .toList();
    }

    /**
     * 更新课程信息
     * @param id 课程ID
     * @param course 更新后的课程信息
     * @return 更新后的课程Optional，如果课程不存在则返回空Optional
     */
    public Optional<Course> update(UUID id, Course course) {
        if (!courses.containsKey(id)) {
            return Optional.empty();
        }
        course.setId(id); // 确保ID一致
        courses.put(id, course);
        return Optional.of(course);
    }

    /**
     * 部分更新课程信息
     * @param id 课程ID
     * @param updates 包含更新字段的Map
     * @return 更新后的课程Optional，如果课程不存在则返回空Optional
     */
    public Optional<Course> partialUpdate(UUID id, Map<String, Object> updates) {
        Course existingCourse = courses.get(id);
        if (existingCourse == null) {
            return Optional.empty();
        }

        updates.forEach((key, value) -> {
            switch (key) {
                case "code" -> existingCourse.setCode((String) value);
                case "title" -> existingCourse.setTitle((String) value);
                case "instructorId" -> existingCourse.setInstructorId((String) value);
                case "scheduleId" -> existingCourse.setScheduleId((String) value);
                case "capacity" -> existingCourse.setCapacity((Integer) value);
            }
        });

        courses.put(id, existingCourse);
        return Optional.of(existingCourse);
    }
}