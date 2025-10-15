package com.zjgsu.ms.hxy.CampusCourseSelectionSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Enrollment 实体类
 * 代表学生选课信息，包含选课记录、成绩和状态管理
 *
 * @author System
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "enrollments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"course_id", "student_id"})
        })
public class Enrollment {

    /**
     * 唯一标识符，系统自动生成 UUID
     */
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * 选课对应的课程 ID，对应 Course 实体的 id，必填
     */
    @NotBlank(message = "课程ID不能为空")
    @Column(name = "course_id", nullable = false)
    private String courseId;

    /**
     * 选课学生的唯一标识符，对应 Student 实体的 id，必填
     */
    @NotBlank(message = "学生ID不能为空")
    @Column(name = "student_id", nullable = false)
    private String studentId;

    /**
     * 选课时间戳，系统自动生成
     */
    @CreationTimestamp
    @Column(name = "enrolled_at", nullable = false, updatable = false)
    private LocalDateTime enrolledAt;

    /**
     * 选课状态（如 "ENROLLED"、"WITHDRAWN"），默认 "ENROLLED"
     */
    @NotBlank(message = "选课状态不能为空")
    @Pattern(regexp = "^(ENROLLED|WITHDRAWN|COMPLETED|FAILED)$",
            message = "选课状态必须是 ENROLLED, WITHDRAWN, COMPLETED 或 FAILED")
    @Column(nullable = false, length = 20)
    private String status = "ENROLLED";

    /**
     * 学生该课程成绩（可为空，课程结束后填入）
     */
    @DecimalMin(value = "0.0", message = "成绩不能低于0分")
    @DecimalMax(value = "100.0", message = "成绩不能高于100分")
    @Column(precision = 5, scale = 2)
    private Double grade;

    // 默认构造函数
    public Enrollment() {
    }

    /**
     * 带参构造函数（不含id、enrolledAt和grade）
     *
     * @param courseId 课程ID
     * @param studentId 学生ID
     */
    public Enrollment(String courseId, String studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.status = "ENROLLED";
    }

    /**
     * 带参构造函数（不含id和enrolledAt）
     *
     * @param courseId 课程ID
     * @param studentId 学生ID
     * @param status 选课状态
     * @param grade 成绩
     */
    public Enrollment(String courseId, String studentId, String status, Double grade) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.status = status;
        this.grade = grade;
    }

    // Getter 和 Setter 方法

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", courseId='" + courseId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", enrolledAt=" + enrolledAt +
                ", status='" + status + '\'' +
                ", grade=" + grade +
                '}';
    }

    /**
     * 检查选课是否处于活跃状态（已选课且未退课）
     *
     * @return true 如果是活跃选课状态，否则 false
     */
    public boolean isActive() {
        return "ENROLLED".equals(status);
    }

    /**
     * 检查选课是否已完成（有成绩）
     *
     * @return true 如果选课已完成，否则 false
     */
    public boolean isCompleted() {
        return grade != null && ("COMPLETED".equals(status) || "FAILED".equals(status));
    }

    /**
     * 获取成绩等级
     *
     * @return 成绩等级（A, B, C, D, F），如果没有成绩返回空字符串
     */
    public String getGradeLevel() {
        if (grade == null) {
            return "";
        }

        if (grade >= 90) return "A";
        else if (grade >= 80) return "B";
        else if (grade >= 70) return "C";
        else if (grade >= 60) return "D";
        else return "F";
    }

    /**
     * 检查是否通过课程（成绩及格）
     *
     * @return true 如果成绩及格（≥60），否则 false；如果没有成绩返回 false
     */
    public boolean isPassed() {
        return grade != null && grade >= 60.0;
    }

    /**
     * 退课操作
     *
     * @return true 如果成功退课，false 如果已经是退课状态或已完成
     */
    public boolean withdraw() {
        if ("WITHDRAWN".equals(status) || isCompleted()) {
            return false;
        }
        this.status = "WITHDRAWN";
        return true;
    }

    /**
     * 完成课程并设置成绩
     *
     * @param finalGrade 最终成绩
     * @return true 如果成功设置，false 如果成绩无效
     */
    public boolean completeCourse(Double finalGrade) {
        if (finalGrade == null || finalGrade < 0 || finalGrade > 100) {
            return false;
        }
        this.grade = finalGrade;
        this.status = finalGrade >= 60.0 ? "COMPLETED" : "FAILED";
        return true;
    }

    /**
     * 获取选课时长（从选课时间到现在的天数）
     *
     * @return 选课时长（天数）
     */
    public long getEnrollmentDurationInDays() {
        if (enrolledAt == null) {
            return 0;
        }
        return java.time.Duration.between(enrolledAt, LocalDateTime.now()).toDays();
    }

    /**
     * 检查是否可以退课（选课时间在2周内且未完成）
     *
     * @return true 如果可以退课，否则 false
     */
    public boolean canWithdraw() {
        return isActive() && getEnrollmentDurationInDays() <= 14;
    }
}