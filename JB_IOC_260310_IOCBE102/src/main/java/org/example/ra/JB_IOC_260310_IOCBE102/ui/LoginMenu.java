package org.example.ra.JB_IOC_260310_IOCBE102.ui;

import org.example.ra.JB_IOC_260310_IOCBE102.model.Admin;
import org.example.ra.JB_IOC_260310_IOCBE102.model.Student;
import org.example.ra.JB_IOC_260310_IOCBE102.service.AppException;
import org.example.ra.JB_IOC_260310_IOCBE102.service.AuthService;
import org.example.ra.JB_IOC_260310_IOCBE102.service.CourseService;
import org.example.ra.JB_IOC_260310_IOCBE102.service.EnrollmentService;
import org.example.ra.JB_IOC_260310_IOCBE102.service.StudentService;

import java.util.Optional;

public class LoginMenu {
    private final Console console;
    private final AuthService authService;
    private final CourseService courseService;
    private final StudentService studentService;
    private final EnrollmentService enrollmentService;

    public LoginMenu(Console console, AuthService authService, CourseService courseService,
                     StudentService studentService, EnrollmentService enrollmentService) {
        this.console = console;
        this.authService = authService;
        this.courseService = courseService;
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
    }

    public void start() {
        while (true) {
            console.title("COURSE MANAGEMENT SYSTEM");
            console.line("1. Login as Admin");
            console.line("2. Login as Student");
            console.line("0. Exit");
            int choice = console.readInt("Choose");
            try {
                switch (choice) {
                    case 1 -> loginAdmin();
                    case 2 -> loginStudent();
                    case 0 -> {
                        console.line("Goodbye.");
                        return;
                    }
                    default -> console.error("Invalid choice.");
                }
            } catch (AppException exception) {
                console.error(exception.getMessage());
            }
        }
    }

    private void loginAdmin() {
        String username = console.readLine("Username");
        String password = console.readLine("Password");
        Optional<Admin> admin = authService.loginAdmin(username, password);
        if (admin.isEmpty()) {
            console.error("Invalid admin credentials.");
            return;
        }
        console.success("Welcome " + admin.get().getFullName());
        new AdminMenu(console, courseService, studentService, enrollmentService).start();
    }

    private void loginStudent() {
        String email = console.readLine("Email");
        String password = console.readLine("Password");
        Optional<Student> student = authService.loginStudent(email, password);
        if (student.isEmpty()) {
            console.error("Invalid student credentials.");
            return;
        }
        console.success("Welcome " + student.get().getName());
        new StudentMenu(console, courseService, enrollmentService, student.get()).start();
    }
}
