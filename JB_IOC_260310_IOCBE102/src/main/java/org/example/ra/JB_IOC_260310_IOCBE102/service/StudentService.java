package org.example.ra.JB_IOC_260310_IOCBE102.service;

import org.example.ra.JB_IOC_260310_IOCBE102.dao.StudentDao;
import org.example.ra.JB_IOC_260310_IOCBE102.model.Student;

import java.util.List;

public class StudentService {
    private final StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public List<Student> findAll() {
        return studentDao.findAll();
    }

    public List<Student> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        return studentDao.search(keyword.trim());
    }

    public Student create(Student student) {
        validate(student);
        return studentDao.create(student);
    }

    public void update(Student student) {
        validate(student);
        if (!studentDao.update(student)) {
            throw new AppException("Student not found.");
        }
    }

    public void delete(int id) {
        if (!studentDao.delete(id)) {
            throw new AppException("Student not found.");
        }
    }

    public Student getRequired(int id) {
        return studentDao.findById(id).orElseThrow(() -> new AppException("Student not found."));
    }

    private static void validate(Student student) {
        if (student.getName() == null || student.getName().isBlank()) {
            throw new AppException("Student name is required.");
        }
        if (student.getEmail() == null || student.getEmail().isBlank() || !student.getEmail().contains("@")) {
            throw new AppException("Valid email is required.");
        }
        if (student.getDob() == null) {
            throw new AppException("Date of birth is required.");
        }
        if (student.getPhone() == null || student.getPhone().isBlank()) {
            throw new AppException("Phone number is required.");
        }
        String phoneDigits = student.getPhone().replaceAll("[^0-9]", "");
        if (phoneDigits.length() < 10 || phoneDigits.length() > 20) {
            throw new AppException("Phone number must be between 10 and 20 digits.");
        }
        if (student.getPassword() == null || student.getPassword().isBlank()) {
            throw new AppException("Password is required.");
        }
    }
}
