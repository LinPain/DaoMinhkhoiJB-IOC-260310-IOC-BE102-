package org.example.ra.JB_IOC_260310_IOCBE102.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DatabaseConfig {
    private static final String CONFIG_FILE = "db.properties";

    private final String url;
    private final String username;
    private final String password;

    private DatabaseConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DatabaseConfig load() {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Cannot read database configuration", exception);
        }

        return new DatabaseConfig(
                firstNonBlank(System.getenv("DB_URL"), properties.getProperty("db.url")),
                firstNonBlank(System.getenv("DB_USERNAME"), properties.getProperty("db.username")),
                firstNonBlank(System.getenv("DB_PASSWORD"), properties.getProperty("db.password"), "")
        );
    }

    public String url() {
        return url;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }
}
