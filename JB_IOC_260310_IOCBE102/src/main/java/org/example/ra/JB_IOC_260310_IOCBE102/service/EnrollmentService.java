package org.example.ra.JB_IOC_260310_IOCBE102.service;

import org.example.ra.JB_IOC_260310_IOCBE102.dao.CourseDao;
import org.example.ra.JB_IOC_260310_IOCBE102.dao.EnrollmentDao;
import org.example.ra.JB_IOC_260310_IOCBE102.dao.StudentDao;
import org.example.ra.JB_IOC_260310_IOCBE102.model.CourseStatistic;
import org.example.ra.JB_IOC_260310_IOCBE102.model.Enrollment;

import java.util.List;

public class EnrollmentService {
    private final EnrollmentDao enrollmentDao;
    private final CourseDao courseDao;
    private final StudentDao studentDao;

    public EnrollmentService(EnrollmentDao enrollmentDao, CourseDao courseDao, StudentDao studentDao) {
        this.enrollmentDao = enrollmentDao;
        this.courseDao = courseDao;
        this.studentDao = studentDao;
    }

    public List<Enrollment> findAll() {
        return enrollmentDao.findAll();
    }

    public void register(int studentId, int courseId) {
        studentDao.findById(studentId).orElseThrow(() -> new AppException("Student not found."));
        courseDao.findById(courseId).orElseThrow(() -> new AppException("Course not found."));
        if (enrollmentDao.exists(studentId, courseId)) {
            throw new AppException("Student is already registered for this course.");
        }
        enrollmentDao.create(studentId, courseId);
    }

    public void delete(int enrollmentId) {
        if (!enrollmentDao.delete(enrollmentId)) {
            throw new AppException("Enrollment not found.");
        }
    }

    public void updateStatus(int enrollmentId, String status) {
        String normalized = status == null ? "" : status.trim().toUpperCase();
        if (!List.of("WAITING", "CONFIRM", "DENIED", "CANCEL").contains(normalized)) {
            throw new AppException("Status must be WAITING, CONFIRM, DENIED, or CANCEL.");
        }
        if (!enrollmentDao.updateStatus(enrollmentId, normalized)) {
            throw new AppException("Enrollment not found.");
        }
    }

    public List<Enrollment> findByCourse(int courseId) {
        courseDao.findById(courseId).orElseThrow(() -> new AppException("Course not found."));
        return enrollmentDao.findByCourse(courseId);
    }

    public List<Enrollment> findByStudent(int studentId) {
        studentDao.findById(studentId).orElseThrow(() -> new AppException("Student not found."));
        return enrollmentDao.findByStudent(studentId);
    }

    public List<CourseStatistic> countStudentsByCourse() {
        return enrollmentDao.countStudentsByCourse();
    }
}
