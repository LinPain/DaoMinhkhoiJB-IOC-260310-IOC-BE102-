package org.example.ra.JB_IOC_260310_IOCBE102.dao;

import org.example.ra.JB_IOC_260310_IOCBE102.config.Database;
import org.example.ra.JB_IOC_260310_IOCBE102.model.Course;
import org.example.ra.JB_IOC_260310_IOCBE102.service.AppException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDao {
    private final Database database;

    public CourseDao(Database database) {
        this.database = database;
    }

    public List<Course> findAll() {
        return findSorted("id");
    }

    public List<Course> findOpenCourses() {
        return findAll();
    }

    public List<Course> findSorted(String sortField) {
        String orderBy = switch (sortField) {
            case "name" -> "name";
            case "duration" -> "duration";
            default -> "id";
        };
        return queryMany("SELECT id, name, duration, instructor, created_at FROM course ORDER BY " + orderBy);
    }

    public Optional<Course> findById(int id) {
        String sql = "SELECT id, name, duration, instructor, created_at FROM course WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(map(resultSet)) : Optional.empty();
            }
        } catch (SQLException exception) {
            throw new AppException("Cannot load course", exception);
        }
    }

    public List<Course> searchByName(String keyword) {
        String sql = """
                SELECT id, name, duration, instructor, created_at
                FROM course
                WHERE LOWER(name) LIKE ?
                ORDER BY name
                """;
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + keyword.toLowerCase() + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                return mapMany(resultSet);
            }
        } catch (SQLException exception) {
            throw new AppException("Cannot search courses", exception);
        }
    }

    public Course create(Course course) {
        String sql = "INSERT INTO course (name, duration, instructor) VALUES (?, ?, ?)";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fill(statement, course);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    course.setId(keys.getInt(1));
                }
            }
            return course;
        } catch (SQLException exception) {
            throw new AppException("Cannot create course", exception);
        }
    }

    public boolean update(Course course) {
        String sql = "UPDATE course SET name = ?, duration = ?, instructor = ? WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fill(statement, course);
            statement.setInt(4, course.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new AppException("Cannot update course", exception);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM course WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new AppException("Cannot delete course", exception);
        }
    }

    private List<Course> queryMany(String sql) {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return mapMany(resultSet);
        } catch (SQLException exception) {
            throw new AppException("Cannot load courses", exception);
        }
    }

    private static List<Course> mapMany(ResultSet resultSet) throws SQLException {
        List<Course> courses = new ArrayList<>();
        while (resultSet.next()) {
            courses.add(map(resultSet));
        }
        return courses;
    }

    private static void fill(PreparedStatement statement, Course course) throws SQLException {
        statement.setString(1, course.getName());
        statement.setInt(2, course.getDuration());
        statement.setString(3, course.getInstructor());
    }

    private static Course map(ResultSet resultSet) throws SQLException {
        Date createdAt = resultSet.getDate("created_at");
        return new Course(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("duration"),
                resultSet.getString("instructor"),
                createdAt == null ? null : createdAt.toLocalDate()
        );
    }
}
