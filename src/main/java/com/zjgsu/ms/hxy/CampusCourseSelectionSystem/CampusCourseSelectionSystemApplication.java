package com.zjgsu.ms.hxy.CampusCourseSelectionSystem;

import com.zjgsu.ms.hxy.CampusCourseSelectionSystem.model.Course;
import com.zjgsu.ms.hxy.CampusCourseSelectionSystem.service.CourseService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CampusCourseSelectionSystemApplication {

    @Autowired
    private CourseService courseService;

	public static void main(String[] args) {
		SpringApplication.run(CampusCourseSelectionSystemApplication.class, args);
	}

    // 如何初始化测试数据？在启动类中添加 @PostConstruct 方法
    @PostConstruct
    public void initData() {
        // 创建测试课程
        // todo
        Course course = new Course();
        courseService.createCourse(course);
    }

}
