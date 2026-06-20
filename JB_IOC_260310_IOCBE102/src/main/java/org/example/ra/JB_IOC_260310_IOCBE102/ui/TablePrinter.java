package org.example.ra.JB_IOC_260310_IOCBE102.ui;

import org.example.ra.JB_IOC_260310_IOCBE102.model.Course;
import org.example.ra.JB_IOC_260310_IOCBE102.model.CourseStatistic;
import org.example.ra.JB_IOC_260310_IOCBE102.model.Enrollment;
import org.example.ra.JB_IOC_260310_IOCBE102.model.Student;

import java.util.List;

public final class TablePrinter {
    private TablePrinter() {
    }

    public static void courses(Console console, List<Course> courses) {
        if (courses.isEmpty()) {
            console.line("No courses found.");
            return;
        }
        console.line(String.format("%-5s %-25s %-10s %s", "ID", "Name", "Duration", "Instructor"));
        for (Course course : courses) {
            console.line(String.format("%-5d %-25s %-10d %s",
                    course.getId(), limit(course.getName(), 25), course.getDuration(), 
                    limit(course.getInstructor(), 40)));
        }
    }

    public static void students(Console console, List<Student> students) {
        if (students.isEmpty()) {
            console.line("No students found.");
            return;
        }
        console.line(String.format("%-5s %-24s %-28s %-14s %-12s", "ID", "Name", "Email", "Phone", "DOB"));
        for (Student student : students) {
            console.line(String.format("%-5d %-24s %-28s %-14s %-12s",
                    student.getId(), limit(student.getName(), 24), limit(student.getEmail(), 28),
                    limit(student.getPhone(), 14), student.getDob()));
        }
    }

    public static void enrollments(Console console, List<Enrollment> enrollments) {
        if (enrollments.isEmpty()) {
            console.line("No enrollments found.");
            return;
        }
        console.line(String.format("%-5s %-24s %-25s %-12s %-15s", "ID", "Student", "Course", "Status", "Date"));
        for (Enrollment enrollment : enrollments) {
            console.line(String.format("%-5d %-24s %-25s %-12s %-15s",
                    enrollment.getId(), limit(enrollment.getStudentName(), 24),
                    limit(enrollment.getCourseName(), 25), enrollment.getStatus(), enrollment.getCreatedAt()));
        }
    }

    public static void statistics(Console console, List<CourseStatistic> statistics) {
        if (statistics.isEmpty()) {
            console.line("No statistics found.");
            return;
        }
        console.line(String.format("%-5s %-30s %-15s", "ID", "Course", "Students"));
        for (CourseStatistic statistic : statistics) {
            console.line(String.format("%-5d %-30s %-15d",
                    statistic.getCourseId(), limit(statistic.getCourseName(), 30), statistic.getStudentCount()));
        }
    }

    private static String limit(String value, int max) {
        if (value == null) {
            return "";
        }
        return value.length() <= max ? value : value.substring(0, max - 3) + "...";
    }
}
