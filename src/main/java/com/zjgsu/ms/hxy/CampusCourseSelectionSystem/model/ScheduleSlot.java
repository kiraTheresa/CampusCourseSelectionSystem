package com.zjgsu.ms.hxy.CampusCourseSelectionSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ScheduleSlot 实体类
 * 代表课程时间安排，包含上课时间、星期和预计出勤信息
 *
 * @author System
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "schedule_slots")
public class ScheduleSlot {

    /**
     * 唯一标识符，系统自动生成 UUID
     */
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * 课程上课星期（如 "MONDAY"），必填，可取值范围：MONDAY ~ SUNDAY
     */
    @NotBlank(message = "上课星期不能为空")
    @Pattern(regexp = "^(MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY)$",
            message = "星期格式不正确，必须是 MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY 或 SUNDAY")
    @Column(name = "day_of_week", nullable = false, length = 10)
    private String dayOfWeek;

    /**
     * 课程开始时间（如 "08:00"），必填
     */
    @NotNull(message = "开始时间不能为空")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    /**
     * 课程结束时间（如 "10:00"），必填
     */
    @NotNull(message = "结束时间不能为空")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    /**
     * 预计出勤人数（如 50），可选
     */
    @Min(value = 0, message = "预计出勤人数不能为负数")
    @Max(value = 1000, message = "预计出勤人数不能超过1000")
    @Column(name = "expected_attendance")
    private Integer expectedAttendance;

    /**
     * 创建时间戳，系统自动生成
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 默认构造函数
    public ScheduleSlot() {
    }

    /**
     * 带参构造函数（不含id和createdAt）
     *
     * @param dayOfWeek 上课星期
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param expectedAttendance 预计出勤人数
     */
    public ScheduleSlot(String dayOfWeek, LocalTime startTime, LocalTime endTime, Integer expectedAttendance) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.expectedAttendance = expectedAttendance;
    }

    /**
     * 带参构造函数（不含expectedAttendance）
     *
     * @param dayOfWeek 上课星期
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public ScheduleSlot(String dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getter 和 Setter 方法

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getExpectedAttendance() {
        return expectedAttendance;
    }

    public void setExpectedAttendance(Integer expectedAttendance) {
        this.expectedAttendance = expectedAttendance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ScheduleSlot{" +
                "id=" + id +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", expectedAttendance=" + expectedAttendance +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * 获取课程时长（分钟）
     *
     * @return 课程时长（分钟）
     */
    public long getDurationInMinutes() {
        if (startTime == null || endTime == null) {
            return 0;
        }
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }

    /**
     * 检查时间安排是否有效（开始时间早于结束时间）
     *
     * @return true 如果时间安排有效，否则 false
     */
    public boolean isValidTimeSlot() {
        return startTime != null && endTime != null && !startTime.isAfter(endTime);
    }

    /**
     * 检查时间是否冲突（与另一个时间安排比较）
     *
     * @param other 另一个时间安排
     * @return true 如果时间冲突，否则 false
     */
    public boolean hasTimeConflict(ScheduleSlot other) {
        if (!this.dayOfWeek.equals(other.dayOfWeek)) {
            return false;
        }

        return (this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime));
    }

    /**
     * 获取时间段的显示字符串
     *
     * @return 格式化的时间段字符串（如 "MONDAY 08:00-10:00"）
     */
    public String getTimeSlotDisplay() {
        return dayOfWeek + " " + startTime + "-" + endTime;
    }

    /**
     * 检查是否为上午时间段（结束时间在12:00之前）
     *
     * @return true 如果是上午时间段，否则 false
     */
    public boolean isMorningSlot() {
        return endTime != null && endTime.isBefore(LocalTime.of(12, 0));
    }

    /**
     * 检查是否为下午时间段（开始时间在12:00之后）
     *
     * @return true 如果是下午时间段，否则 false
     */
    public boolean isAfternoonSlot() {
        return startTime != null && startTime.isAfter(LocalTime.of(12, 0));
    }
}