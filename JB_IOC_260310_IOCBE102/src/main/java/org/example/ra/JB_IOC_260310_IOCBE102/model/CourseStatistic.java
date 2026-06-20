package org.example.ra.JB_IOC_260310_IOCBE102.model;

public class CourseStatistic {
    private final int courseId;
    private final String courseName;
    private final int studentCount;

    public CourseStatistic(int courseId, String courseName, int studentCount) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.studentCount = studentCount;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getStudentCount() {
        return studentCount;
    }
}
