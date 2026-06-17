package org.example.ra.JB_IOC_260310_IOCBE102.dao;

import org.example.ra.JB_IOC_260310_IOCBE102.config.Database;
import org.example.ra.JB_IOC_260310_IOCBE102.model.CourseStatistic;
import org.example.ra.JB_IOC_260310_IOCBE102.model.Enrollment;
import org.example.ra.JB_IOC_260310_IOCBE102.service.AppException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDao {
    private final Database database;

    public EnrollmentDao(Database database) {
        this.database = database;
    }

    public boolean exists(int studentId, int courseId) {
        String sql = "SELECT 1 FROM enrollment WHERE student_id = ? AND course_id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            throw new AppException("Cannot check enrollment", exception);
        }
    }

    public void create(int studentId, int courseId) {
        String sql = "INSERT INTO enrollment (student_id, course_id, status) VALUES (?, ?, 'WAITING')";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new AppException("Cannot register course. Student may already be registered.", exception);
        }
    }

    public boolean updateStatus(int enrollmentId, String status) {
        String sql = "UPDATE enrollment SET status = ? WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, enrollmentId);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new AppException("Cannot update enrollment status", exception);
        }
    }

    public boolean delete(int enrollmentId) {
        String sql = "DELETE FROM enrollment WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, enrollmentId);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new AppException("Cannot delete enrollment", exception);
        }
    }

    public List<Enrollment> findAll() {
        return queryEnrollments(baseSelect() + " ORDER BY e.id");
    }

    public List<Enrollment> findByCourse(int courseId) {
        String sql = baseSelect() + " WHERE e.course_id = ? ORDER BY s.name";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            return executeEnrollmentQuery(statement);
        } catch (SQLException exception) {
            throw new AppException("Cannot load enrollments by course", exception);
        }
    }

    public List<Enrollment> findByStudent(int studentId) {
        String sql = baseSelect() + " WHERE e.student_id = ? ORDER BY e.created_at DESC, c.name";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            return executeEnrollmentQuery(statement);
        } catch (SQLException exception) {
            throw new AppException("Cannot load enrollments by student", exception);
        }
    }

    public List<CourseStatistic> countStudentsByCourse() {
        String sql = """
                SELECT c.id AS course_id, c.name AS course_name, COUNT(e.id) AS student_count
                FROM course c
                LEFT JOIN enrollment e ON e.course_id = c.id
                GROUP BY c.id, c.name
                ORDER BY student_count DESC, c.name
                """;
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<CourseStatistic> statistics = new ArrayList<>();
            while (resultSet.next()) {
                statistics.add(new CourseStatistic(
                        resultSet.getInt("course_id"),
                        resultSet.getString("course_name"),
                        resultSet.getInt("student_count")
                ));
            }
            return statistics;
        } catch (SQLException exception) {
            throw new AppException("Cannot load course statistics", exception);
        }
    }

    private List<Enrollment> queryEnrollments(String sql) {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            return executeEnrollmentQuery(statement);
        } catch (SQLException exception) {
            throw new AppException("Cannot load enrollments", exception);
        }
    }

    private List<Enrollment> executeEnrollmentQuery(PreparedStatement statement) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery()) {
            List<Enrollment> enrollments = new ArrayList<>();
            while (resultSet.next()) {
                Timestamp registeredAt = resultSet.getTimestamp("registered_at");
                enrollments.add(new Enrollment(
                        resultSet.getInt("id"),
                        resultSet.getInt("student_id"),
                        resultSet.getInt("course_id"),
                        registeredAt == null ? null : registeredAt.toLocalDateTime(),
                        resultSet.getString("status"),
                        resultSet.getString("student_name"),
                        resultSet.getString("course_name")
                ));
            }
            return enrollments;
        }
    }

    private static String baseSelect() {
        return """
                SELECT e.id, e.student_id, e.course_id, e.registered_at, e.status,
                       s.name AS student_name, c.name AS course_name
                FROM enrollment e
                JOIN student s ON s.id = e.student_id
                JOIN course c ON c.id = e.course_id
                """;
    }
}
