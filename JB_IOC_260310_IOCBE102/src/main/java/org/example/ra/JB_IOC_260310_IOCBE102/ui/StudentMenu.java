package org.example.ra.JB_IOC_260310_IOCBE102.ui;

import org.example.ra.JB_IOC_260310_IOCBE102.model.Student;
import org.example.ra.JB_IOC_260310_IOCBE102.service.AppException;
import org.example.ra.JB_IOC_260310_IOCBE102.service.CourseService;
import org.example.ra.JB_IOC_260310_IOCBE102.service.EnrollmentService;

public class StudentMenu {
    private final Console console;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final Student student;

    public StudentMenu(Console console, CourseService courseService, EnrollmentService enrollmentService, Student student) {
        this.console = console;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.student = student;
    }

    public void start() {
        while (true) {
            console.title("STUDENT MENU");
            console.line("1. View open courses");
            console.line("2. Register a course");
            console.line("3. View my registered courses");
            console.line("0. Logout");
            int choice = console.readInt("Choose");
            try {
                switch (choice) {
                    case 1 -> TablePrinter.courses(console, courseService.findOpenCourses());
                    case 2 -> registerCourse();
                    case 3 -> TablePrinter.enrollments(console, enrollmentService.findByStudent(student.getId()));
                    case 0 -> {
                        return;
                    }
                    default -> console.error("Invalid choice.");
                }
            } catch (AppException exception) {
                console.error(exception.getMessage());
            }
        }
    }

    private void registerCourse() {
        TablePrinter.courses(console, courseService.findOpenCourses());
        int courseId = console.readInt("Course ID");
        enrollmentService.register(student.getId(), courseId);
        console.success("Registered successfully.");
    }
}
