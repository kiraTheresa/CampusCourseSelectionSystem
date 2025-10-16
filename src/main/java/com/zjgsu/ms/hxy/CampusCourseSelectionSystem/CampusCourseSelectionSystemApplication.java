package com.zjgsu.ms.hxy.CampusCourseSelectionSystem;

import com.zjgsu.ms.hxy.CampusCourseSelectionSystem.model.Course;
import com.zjgsu.ms.hxy.CampusCourseSelectionSystem.model.Student;
import com.zjgsu.ms.hxy.CampusCourseSelectionSystem.service.CourseService;
import com.zjgsu.ms.hxy.CampusCourseSelectionSystem.service.EnrollmentService;
import com.zjgsu.ms.hxy.CampusCourseSelectionSystem.service.StudentService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CampusCourseSelectionSystemApplication {

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private EnrollmentService enrollmentService;

    public static void main(String[] args) {
        SpringApplication.run(CampusCourseSelectionSystemApplication.class, args);
    }

    @PostConstruct
    public void initData() {
        // 检查是否已经初始化过
        if (courseService.isInitialized() || studentService.isInitialized()) {
            System.out.println("=== 测试数据已存在，跳过初始化 ===");
            return;
        }

        System.out.println("=== 开始初始化测试数据 ===");

        try {
            // 1. 创建测试课程
            initCourses();

            // 2. 创建测试学生
            initStudents();

            // 3. 创建选课记录
            initEnrollments();

            System.out.println("=== 测试数据初始化完成 ===");
            printStatistics();

        } catch (Exception e) {
            System.err.println("初始化测试数据时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initCourses() {
        System.out.println("创建测试课程...");

        try {
            Course course1 = new Course("CS101", "计算机科学导论", "INS001", "SCH001", 50);
            courseService.createCourse(course1);
            System.out.println("创建课程: " + course1.getCode() + " - " + course1.getTitle());

            Course course2 = new Course("MATH201", "高等数学", "INS002", "SCH002", 60);
            courseService.createCourse(course2);
            System.out.println("创建课程: " + course2.getCode() + " - " + course2.getTitle());

            Course course3 = new Course("ENG101", "大学英语", "INS003", "SCH003", 40);
            courseService.createCourse(course3);
            System.out.println("创建课程: " + course3.getCode() + " - " + course3.getTitle());

            Course course4 = new Course("PHY301", "大学物理", "INS004", "SCH004", 45);
            courseService.createCourse(course4);
            System.out.println("创建课程: " + course4.getCode() + " - " + course4.getTitle());

        } catch (Exception e) {
            System.err.println("创建课程失败: " + e.getMessage());
        }
    }

    private void initStudents() {
        System.out.println("创建测试学生...");

        try {
            Student student1 = new Student("2024001", "张三", "计算机科学与技术", 2024, "zhangsan@zjgsu.edu.cn");
            studentService.createStudent(student1);
            System.out.println("创建学生: " + student1.getStudentId() + " - " + student1.getName());

            Student student2 = new Student("2024002", "李四", "软件工程", 2024, "lisi@zjgsu.edu.cn");
            studentService.createStudent(student2);
            System.out.println("创建学生: " + student2.getStudentId() + " - " + student2.getName());

            Student student3 = new Student("2024003", "王五", "数据科学", 2024, "wangwu@zjgsu.edu.cn");
            studentService.createStudent(student3);
            System.out.println("创建学生: " + student3.getStudentId() + " - " + student3.getName());

            Student student4 = new Student("2024004", "赵六", "人工智能", 2024, "zhaoliu@zjgsu.edu.cn");
            studentService.createStudent(student4);
            System.out.println("创建学生: " + student4.getStudentId() + " - " + student4.getName());

        } catch (Exception e) {
            System.err.println("创建学生失败: " + e.getMessage());
        }
    }

    private void initEnrollments() {
        System.out.println("创建测试选课记录...");

        try {
            // 获取课程和学生信息
            var cs101 = courseService.getCourseByCode("CS101").orElseThrow();
            var math201 = courseService.getCourseByCode("MATH201").orElseThrow();
            var eng101 = courseService.getCourseByCode("ENG101").orElseThrow();

            var student1 = studentService.getStudentByStudentId("2024001").orElseThrow();
            var student2 = studentService.getStudentByStudentId("2024002").orElseThrow();
            var student3 = studentService.getStudentByStudentId("2024003").orElseThrow();
            var student4 = studentService.getStudentByStudentId("2024004").orElseThrow();

            // 学生1选3门课
            enrollmentService.enrollCourse(cs101.getId().toString(), student1.getId().toString());
            enrollmentService.enrollCourse(math201.getId().toString(), student1.getId().toString());
            enrollmentService.enrollCourse(eng101.getId().toString(), student1.getId().toString());
            System.out.println("学生 " + student1.getName() + " 选课3门");

            // 学生2选2门课
            enrollmentService.enrollCourse(cs101.getId().toString(), student2.getId().toString());
            enrollmentService.enrollCourse(math201.getId().toString(), student2.getId().toString());
            System.out.println("学生 " + student2.getName() + " 选课2门");

            // 学生3选1门课
            enrollmentService.enrollCourse(eng101.getId().toString(), student3.getId().toString());
            System.out.println("学生 " + student3.getName() + " 选课1门");

            // 学生4选2门课
            enrollmentService.enrollCourse(cs101.getId().toString(), student4.getId().toString());
            enrollmentService.enrollCourse(eng101.getId().toString(), student4.getId().toString());
            System.out.println("学生 " + student4.getName() + " 选课2门");

            // 测试退课功能
            enrollmentService.withdrawCourse(eng101.getId().toString(), student1.getId().toString());
            System.out.println("学生 " + student1.getName() + " 退选英语课");

            // 测试成绩录入
            var enrollment = enrollmentService.getEnrollmentsByStudent(student1.getId().toString())
                    .stream()
                    .filter(e -> e.getCourseId().equals(cs101.getId().toString()))
                    .findFirst()
                    .orElseThrow();

            enrollmentService.updateGrade(enrollment.getId(), 85.5);
            System.out.println("为学生 " + student1.getName() + " 的计算机课程录入成绩: 85.5");

        } catch (Exception e) {
            System.err.println("创建选课记录失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void printStatistics() {
        System.out.println("\n=== 系统统计信息 ===");
        System.out.println("课程总数: " + courseService.getCourseCount());
        System.out.println("学生总数: " + studentService.getStudentCount());
        System.out.println("选课记录总数: " + enrollmentService.getEnrollmentCount());

        // 各课程选课人数
        courseService.getAllCourses().forEach(course -> {
            long enrollmentCount = enrollmentService.getEnrollmentCountByCourse(course.getId().toString());
            System.out.println("课程 " + course.getCode() + " 选课人数: " + enrollmentCount + "/" + course.getCapacity());
        });

        // 各学生选课情况
        studentService.getAllStudents().forEach(student -> {
            long enrollmentCount = enrollmentService.getEnrollmentCountByStudent(student.getId().toString());
            System.out.println("学生 " + student.getName() + " 选课数量: " + enrollmentCount);
        });
    }
}