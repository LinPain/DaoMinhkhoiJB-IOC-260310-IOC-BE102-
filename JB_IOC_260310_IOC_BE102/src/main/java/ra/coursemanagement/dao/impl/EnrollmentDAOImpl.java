package ra.coursemanagement.dao.impl;

import ra.coursemanagement.dao.EnrollmentDAO;
import ra.coursemanagement.model.Enrollment;
import ra.coursemanagement.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnrollmentDAOImpl implements EnrollmentDAO {

    @Override
    public List<Enrollment> getAll() {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollment ORDER BY id ASC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Enrollment(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getInt("course_id"),
                        rs.getTimestamp("registered_at").toLocalDateTime(),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean add(int studentId, int courseId) {
        String sql = "INSERT INTO enrollment (student_id, course_id) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateStatus(int enrollmentId, String status) {
        // Chú ý: Ở PostgreSQL khi cập nhật enum từ JDBC, cần ép kiểu tường minh dạng ?::enrollment_status
        String sql = "UPDATE enrollment SET status = ?::enrollment_status WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.toUpperCase());
            ps.setInt(2, enrollmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkDuplicate(int studentId, int courseId) {
        String sql = "SELECT COUNT(*) FROM enrollment WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> getStudentHistory(int studentId) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT e.id, c.name AS course_name, e.registered_at, e.status " +
                "FROM enrollment e JOIN course c ON e.course_id = c.id " +
                "WHERE e.student_id = ? ORDER BY e.registered_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", rs.getInt("id"));
                    map.put("course_name", rs.getString("course_name"));
                    map.put("registered_at", rs.getTimestamp("registered_at").toLocalDateTime());
                    map.put("status", rs.getString("status"));
                    list.add(map);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getCourseStatistics() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT c.id, c.name AS course_name, COUNT(e.id) AS total_students " +
                "FROM course c LEFT JOIN enrollment e ON c.id = e.course_id AND e.status = 'CONFIRMED' " +
                "GROUP BY c.id, c.name ORDER BY total_students DESC, c.id ASC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("course_id", rs.getInt("id"));
                map.put("course_name", rs.getString("course_name"));
                map.put("total_students", rs.getInt("total_students"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getEnrollmentsWithDetails() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT e.id AS enrollment_id, s.name AS student_name, c.name AS course_name, " +
                "e.registered_at, e.status " +
                "FROM enrollment e " +
                "JOIN student s ON e.student_id = s.id " +
                "JOIN course c ON e.course_id = c.id " +
                "ORDER BY e.status DESC, e.registered_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("enrollment_id", rs.getInt("enrollment_id"));
                map.put("student_name", rs.getString("student_name"));
                map.put("course_name", rs.getString("course_name"));
                map.put("registered_at", rs.getTimestamp("registered_at").toLocalDateTime());
                map.put("status", rs.getString("status"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
