package org.example.ra.JB_IOC_260310_IOCBE102.model;

import java.time.LocalDateTime;

public class Enrollment {
    private final int id;
    private final int studentId;
    private final int courseId;
    private final LocalDateTime registeredAt;
    private final String status;
    private final String studentName;
    private final String courseName;

    public Enrollment(int id, int studentId, int courseId, LocalDateTime registeredAt,
                      String status, String studentName, String courseName) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.registeredAt = registeredAt;
        this.status = status;
        this.studentName = studentName;
        this.courseName = courseName;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public LocalDateTime getCreatedAt() {
        return registeredAt;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public String getStatus() {
        return status;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getCourseName() {
        return courseName;
    }
}
