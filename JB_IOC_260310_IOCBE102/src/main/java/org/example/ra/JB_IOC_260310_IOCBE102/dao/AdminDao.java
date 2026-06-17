package org.example.ra.JB_IOC_260310_IOCBE102.dao;

import org.example.ra.JB_IOC_260310_IOCBE102.config.Database;
import org.example.ra.JB_IOC_260310_IOCBE102.model.Admin;
import org.example.ra.JB_IOC_260310_IOCBE102.service.AppException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdminDao {
    private final Database database;

    public AdminDao(Database database) {
        this.database = database;
    }

    public Optional<Admin> findByCredentials(String username, String password) {
        String sql = "SELECT id, username FROM admin WHERE username = ? AND password = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String adminUsername = resultSet.getString("username");
                    return Optional.of(new Admin(resultSet.getInt("id"), adminUsername, adminUsername));
                }
                return Optional.empty();
            }
        } catch (SQLException exception) {
            throw new AppException("Cannot load admin account", exception);
        }
    }
}
