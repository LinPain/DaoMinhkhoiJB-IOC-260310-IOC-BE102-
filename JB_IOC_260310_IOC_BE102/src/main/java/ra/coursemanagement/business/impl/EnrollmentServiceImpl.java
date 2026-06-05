package ra.coursemanagement.business.impl;

import ra.coursemanagement.business.EnrollmentService;
import ra.coursemanagement.dao.CourseDAO;
import ra.coursemanagement.dao.EnrollmentDAO;
import ra.coursemanagement.dao.impl.CourseDAOImpl;
import ra.coursemanagement.dao.impl.EnrollmentDAOImpl;

import java.util.List;
import java.util.Map;

public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAOImpl();
    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public String studentRegisterCourse(int studentId, int courseId) {
        // Kiểm tra xem khóa học có thực sự tồn tại
        if (courseDAO.getById(courseId) == null) {
            return "Khóa học không tồn tại hoặc đã bị đóng!";
        }
        // Chống đăng ký trùng lặp môn học theo yêu cầu của đặc tả dự án
        if (enrollmentDAO.checkDuplicate(studentId, courseId)) {
            return "Học viên này đã gửi yêu cầu hoặc đang tham gia khóa học này rồi!";
        }

        return enrollmentDAO.add(studentId, courseId) ? "Thành công" : "Lỗi: Không thể gửi yêu cầu đăng ký.";
    }

    @Override
    public boolean adminReviewEnrollment(int enrollmentId, String status) {
        // Chuẩn hóa chuỗi trạng thái phù hợp với ENUM của PostgreSQL
        String targetStatus = status.trim().toUpperCase();
        if ("CONFIRMED".equals(targetStatus) || "DENIED".equals(targetStatus) || "CANCEL".equals(targetStatus)) {
            return enrollmentDAO.updateStatus(enrollmentId, targetStatus);
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> getHistoryByStudent(int studentId) {
        return enrollmentDAO.getStudentHistory(studentId);
    }

    @Override
    public List<Map<String, Object>> getCourseStatisticsReport() {
        return enrollmentDAO.getCourseStatistics();
    }

    @Override
    public List<Map<String, Object>> getPendingAndAllEnrollments() {
        return enrollmentDAO.getEnrollmentsWithDetails();
    }
}
