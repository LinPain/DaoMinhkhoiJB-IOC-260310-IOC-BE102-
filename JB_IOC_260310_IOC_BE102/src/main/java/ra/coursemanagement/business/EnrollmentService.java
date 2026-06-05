package ra.coursemanagement.business;

import java.util.List;
import java.util.Map;

public interface EnrollmentService {
    String studentRegisterCourse(int studentId, int courseId);
    boolean adminReviewEnrollment(int enrollmentId, String status);
    List<Map<String, Object>> getHistoryByStudent(int studentId);
    List<Map<String, Object>> getCourseStatisticsReport();
    List<Map<String, Object>> getPendingAndAllEnrollments();
}
