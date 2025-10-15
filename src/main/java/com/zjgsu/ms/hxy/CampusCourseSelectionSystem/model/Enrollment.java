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

}