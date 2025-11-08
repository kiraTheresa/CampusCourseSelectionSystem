package com.zjgsu.ms.hxy.CampusCourseSelectionSystem.repository;

import com.zjgsu.ms.hxy.CampusCourseSelectionSystem.model.ScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * ScheduleSlotRepository 接口
 * 使用 Spring Data JPA 实现数据库持久化，提供课程时间安排的 CRUD 操作
 *
 * @author System
 * @version 1.1.0
 * @since 2024
 */
@Repository
public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, UUID> {

    /**
     * 根据时间段ID查找时间安排
     * @param scheduleId 时间段ID
     * @return 包含时间安排的Optional，如果不存在则返回空Optional
     */
    Optional<ScheduleSlot> findByScheduleId(String scheduleId); //

    /**
     * 根据星期几查找时间安排
     * @param dayOfWeek 星期几
     * @return 该星期几的所有时间安排列表
     */
    List<ScheduleSlot> findByDayOfWeek(String dayOfWeek);

    /**
     * 根据开始时间查找时间安排
     * @param startTime 开始时间
     * @return 从该时间开始的所有时间安排列表
     */
    List<ScheduleSlot> findByStartTime(LocalTime startTime); // 修正：参数改为 LocalTime

    /**
     * 检查时间段ID是否存在
     * @param scheduleId 时间段ID
     * @return 如果时间段ID存在返回true，否则返回false
     */
    boolean existsByScheduleId(String scheduleId);
}