package com.zjgsu.ms.hxy.CampusCourseSelectionSystem.controller;

import com.zjgsu.ms.hxy.CampusCourseSelectionSystem.repository.CourseRepository;
import com.zjgsu.ms.hxy.CampusCourseSelectionSystem.repository.EnrollmentRepository;
import com.zjgsu.ms.hxy.CampusCourseSelectionSystem.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 * 提供应用和数据库的健康检查接口
 *
 * @author System
 * @version 1.1
 * @since 2024
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    /**
     * 数据库健康检查接口
     * GET /health/db
     * 检查数据库连接和基本查询是否正常
     *
     * @return 健康检查结果
     */
    @GetMapping("/db")
    public ResponseEntity<Map<String, Object>> checkDatabaseHealth() {
        Map<String, Object> health = new HashMap<>();
        boolean isHealthy = true;
        String message = "数据库连接正常";

        try {
            // 1. 检查数据库连接
            try (Connection connection = dataSource.getConnection()) {
                boolean isValid = connection.isValid(2); // 2秒超时
                if (!isValid) {
                    isHealthy = false;
                    message = "数据库连接无效";
                    health.put("connection", "invalid");
                } else {
                    health.put("connection", "ok");
                }
            }

            // 2. 检查 JPA 实体管理器
            try {
                entityManager.getEntityManagerFactory();
                health.put("jpa", "ok");
            } catch (Exception e) {
                isHealthy = false;
                message = "JPA 实体管理器异常: " + e.getMessage();
                health.put("jpa", "error");
            }

            // 3. 检查 Repository 基本查询
            try {
                long courseCount = courseRepository.count();
                long studentCount = studentRepository.count();
                long enrollmentCount = enrollmentRepository.count();

                health.put("repositories", Map.of(
                        "course", "ok",
                        "student", "ok",
                        "enrollment", "ok"
                ));
                health.put("statistics", Map.of(
                        "courses", courseCount,
                        "students", studentCount,
                        "enrollments", enrollmentCount
                ));
            } catch (Exception e) {
                isHealthy = false;
                message = "Repository 查询异常: " + e.getMessage();
                health.put("repositories", "error");
            }

            // 4. 数据库信息
            try (Connection connection = dataSource.getConnection()) {
                String url = connection.getMetaData().getURL();
                String driverName = connection.getMetaData().getDriverName();
                health.put("database", Map.of(
                        "url", url,
                        "driver", driverName
                ));
            } catch (SQLException e) {
                health.put("database", "unknown");
            }

        } catch (SQLException e) {
            isHealthy = false;
            message = "数据库连接失败: " + e.getMessage();
            health.put("connection", "failed");
            health.put("error", e.getMessage());
        } catch (Exception e) {
            isHealthy = false;
            message = "健康检查失败: " + e.getMessage();
            health.put("error", e.getMessage());
        }

        health.put("status", isHealthy ? "UP" : "DOWN");
        health.put("message", message);
        health.put("timestamp", System.currentTimeMillis());

        HttpStatus status = isHealthy ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        return ResponseEntity.status(status).body(health);
    }

    /**
     * 应用健康检查接口
     * GET /health
     * 基本的应用健康检查
     *
     * @return 健康检查结果
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("message", "应用运行正常");
        health.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(health);
    }
}

