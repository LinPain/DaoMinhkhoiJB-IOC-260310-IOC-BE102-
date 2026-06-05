package ra.coursemanagement.dao;

import ra.coursemanagement.model.Enrollment;
import java.util.List;
import java.util.Map;

public interface EnrollmentDAO {
    List<Enrollment> getAll();
    boolean add(int studentId, int courseId);
    boolean updateStatus(int enrollmentId, String status);
    boolean checkDuplicate(int studentId, int courseId);
    List<Map<String, Object>> getStudentHistory(int studentId);
    List<Map<String, Object>> getCourseStatistics();
    List<Map<String, Object>> getEnrollmentsWithDetails();
}