package org.example;

import org.example.ra.JB_IOC_260310_IOCBE102.config.Database;
import org.example.ra.JB_IOC_260310_IOCBE102.dao.AdminDao;
import org.example.ra.JB_IOC_260310_IOCBE102.dao.CourseDao;
import org.example.ra.JB_IOC_260310_IOCBE102.dao.EnrollmentDao;
import org.example.ra.JB_IOC_260310_IOCBE102.dao.StudentDao;
import org.example.ra.JB_IOC_260310_IOCBE102.service.AuthService;
import org.example.ra.JB_IOC_260310_IOCBE102.service.CourseService;
import org.example.ra.JB_IOC_260310_IOCBE102.service.EnrollmentService;
import org.example.ra.JB_IOC_260310_IOCBE102.service.StudentService;
import org.example.ra.JB_IOC_260310_IOCBE102.ui.Console;
import org.example.ra.JB_IOC_260310_IOCBE102.ui.LoginMenu;

public class Main {
    public static void main(String[] args) {
        Database database = new Database();
        CourseDao courseDao = new CourseDao(database);
        StudentDao studentDao = new StudentDao(database);
        EnrollmentDao enrollmentDao = new EnrollmentDao(database);

        AuthService authService = new AuthService(new AdminDao(database), studentDao);
        CourseService courseService = new CourseService(courseDao);
        StudentService studentService = new StudentService(studentDao);
        EnrollmentService enrollmentService = new EnrollmentService(enrollmentDao, courseDao, studentDao);

        Console console = new Console(System.in, System.out);
        new LoginMenu(console, authService, courseService, studentService, enrollmentService).start();
    }
}
