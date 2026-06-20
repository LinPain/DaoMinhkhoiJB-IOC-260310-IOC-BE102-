package org.example.ra.JB_IOC_260310_IOCBE102.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final DatabaseConfig config;

    public Database() {
        this(DatabaseConfig.load());
    }

    public Database(DatabaseConfig config) {
        this.config = config;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(config.url(), config.username(), config.password());
    }
}
