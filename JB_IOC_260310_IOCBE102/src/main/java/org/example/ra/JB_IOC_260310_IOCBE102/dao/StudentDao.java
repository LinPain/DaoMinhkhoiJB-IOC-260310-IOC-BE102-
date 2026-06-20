package org.example.ra.JB_IOC_260310_IOCBE102.dao;

import org.example.ra.JB_IOC_260310_IOCBE102.config.Database;
import org.example.ra.JB_IOC_260310_IOCBE102.model.Student;
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

public class StudentDao {
    private final Database database;

    public StudentDao(Database database) {
        this.database = database;
    }

    public List<Student> findAll() {
        String sql = "SELECT id, name, dob, email, sex, phone, password, created_at FROM student ORDER BY id";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<Student> students = new ArrayList<>();
            while (resultSet.next()) {
                students.add(map(resultSet));
            }
            return students;
        } catch (SQLException exception) {
            throw new AppException("Cannot load students", exception);
        }
    }

    public Optional<Student> findById(int id) {
        String sql = "SELECT id, name, dob, email, sex, phone, password, created_at FROM student WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(map(resultSet)) : Optional.empty();
            }
        } catch (SQLException exception) {
            throw new AppException("Cannot load student", exception);
        }
    }

    public Optional<Student> findByCredentials(String email, String password) {
        String sql = "SELECT id, name, dob, email, sex, phone, password, created_at FROM student WHERE email = ? AND password = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(map(resultSet)) : Optional.empty();
            }
        } catch (SQLException exception) {
            throw new AppException("Cannot load student account", exception);
        }
    }

    public List<Student> search(String keyword) {
        String sql = """
                SELECT id, name, dob, email, sex, phone, password, created_at
                FROM student
                WHERE LOWER(name) LIKE ? OR LOWER(email) LIKE ?
                ORDER BY name
                """;
        String pattern = "%" + keyword.toLowerCase() + "%";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pattern);
            statement.setString(2, pattern);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Student> students = new ArrayList<>();
                while (resultSet.next()) {
                    students.add(map(resultSet));
                }
                return students;
            }
        } catch (SQLException exception) {
            throw new AppException("Cannot search students", exception);
        }
    }

    public Student create(Student student) {
        String sql = "INSERT INTO student (name, dob, email, sex, phone, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fill(statement, student);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    student.setId(keys.getInt(1));
                }
            }
            return student;
        } catch (SQLException exception) {
            throw new AppException("Cannot create student. Email may already exist.", exception);
        }
    }

    public boolean update(Student student) {
        String sql = """
                UPDATE student
                SET name = ?, dob = ?, email = ?, sex = ?, phone = ?, password = ?
                WHERE id = ?
                """;
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fill(statement, student);
            statement.setInt(7, student.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new AppException("Cannot update student. Email may already exist.", exception);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM student WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new AppException("Cannot delete student", exception);
        }
    }

    private static void fill(PreparedStatement statement, Student student) throws SQLException {
        statement.setString(1, student.getName());
        statement.setDate(2, Date.valueOf(student.getDob()));
        statement.setString(3, student.getEmail());
        statement.setBoolean(4, student.isMale());
        statement.setString(5, student.getPhone());
        statement.setString(6, student.getPassword());
    }

    private static Student map(ResultSet resultSet) throws SQLException {
        Date createdAt = resultSet.getDate("created_at");
        return new Student(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getDate("dob").toLocalDate(),
                resultSet.getString("email"),
                resultSet.getBoolean("sex"),
                resultSet.getString("phone"),
                resultSet.getString("password"),
                createdAt == null ? null : createdAt.toLocalDate()
        );
    }
}
