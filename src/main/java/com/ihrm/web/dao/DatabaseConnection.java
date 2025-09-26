package com.ihrm.web.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static HikariDataSource dataSource;
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    static {
        HikariConfig config = new HikariConfig();

        // Load database configuration from environment variables
        String dbHost = getEnvOrDefault("DB_HOST", "localhost");
        String dbPort = getEnvOrDefault("DB_PORT", "3306");
        String dbName = getEnvOrDefault("DB_NAME", "iHRM");
        String dbUsername = getEnvOrDefault("DB_USERNAME", "root");
        String dbPassword = getEnvOrDefault("DB_PASSWORD", "");

        String jdbcUrl = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC",
                dbHost, dbPort, dbName);

        config.setJdbcUrl(jdbcUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // Load pool configuration from environment or use defaults
        config.setMaximumPoolSize(Integer.parseInt(getEnvOrDefault("DB_POOL_SIZE", "10")));
        config.setMinimumIdle(Integer.parseInt(getEnvOrDefault("DB_MIN_IDLE", "5")));
        config.setIdleTimeout(Long.parseLong(getEnvOrDefault("DB_IDLE_TIMEOUT", "300000")));
        config.setConnectionTimeout(Long.parseLong(getEnvOrDefault("DB_CONNECTION_TIMEOUT", "20000")));
        config.setMaxLifetime(Long.parseLong(getEnvOrDefault("DB_MAX_LIFETIME", "1200000")));

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        try {
            dataSource = new HikariDataSource(config);
            System.out.println("Database connection pool initialized successfully.");
            System.out.println("Connected to: " + jdbcUrl);
        } catch (Exception e) {
            System.err.println("Failed to initialize database connection pool!");
            System.err.println("Please check your .env file configuration.");
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    private static String getEnvOrDefault(String key, String defaultValue) {
        // First try system environment variables
        String value = System.getenv(key);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        // Then try .env file
        value = dotenv.get(key);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        // Finally return default
        return defaultValue;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeConnection() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}