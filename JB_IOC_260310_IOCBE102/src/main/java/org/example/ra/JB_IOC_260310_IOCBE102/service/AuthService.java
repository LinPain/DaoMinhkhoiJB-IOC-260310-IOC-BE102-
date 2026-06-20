package org.example.ra.JB_IOC_260310_IOCBE102.service;

import org.example.ra.JB_IOC_260310_IOCBE102.dao.AdminDao;
import org.example.ra.JB_IOC_260310_IOCBE102.dao.StudentDao;
import org.example.ra.JB_IOC_260310_IOCBE102.model.Admin;
import org.example.ra.JB_IOC_260310_IOCBE102.model.Student;
import org.example.ra.JB_IOC_260310_IOCBE102.model.UserRole;

import java.util.Optional;

public class AuthService {
    private final AdminDao adminDao;
    private final StudentDao studentDao;

    public AuthService(AdminDao adminDao, StudentDao studentDao) {
        this.adminDao = adminDao;
        this.studentDao = studentDao;
    }

    public Optional<Admin> loginAdmin(String username, String password) {
        validateLogin(username, password);
        return adminDao.findByCredentials(username.trim(), password);
    }

    public Optional<Student> loginStudent(String email, String password) {
        validateLogin(email, password);
        return studentDao.findByCredentials(email.trim(), password);
    }

    public String label(UserRole role) {
        return role == UserRole.ADMIN ? "Admin" : "Student";
    }

    private static void validateLogin(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new AppException("Login and password are required.");
        }
    }
}
