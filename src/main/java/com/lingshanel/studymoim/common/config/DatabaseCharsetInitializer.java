package com.lingshanel.studymoim.common.config;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class DatabaseCharsetInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseCharsetInitializer.class);

    private static final List<String> TABLES = List.of(
            "users",
            "categories",
            "study_posts",
            "comments",
            "likes",
            "bookmarks",
            "post_reports",
            "password_reset_verifications",
            "admin_action_logs"
    );

    private final JdbcTemplate jdbcTemplate;

    public DatabaseCharsetInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
            if (!isMySql(connection)) {
                return null;
            }

            try (Statement statement = connection.createStatement()) {
                updateDatabaseCharset(connection, statement);
                for (String table : TABLES) {
                    updateTableCharset(statement, table);
                }
            }

            return null;
        });
    }

    private boolean isMySql(Connection connection) {
        try {
            return connection.getMetaData().getDatabaseProductName().toLowerCase().contains("mysql");
        } catch (Exception exception) {
            log.debug("Could not detect database product name.", exception);
            return false;
        }
    }

    private void updateDatabaseCharset(Connection connection, Statement statement) {
        try {
            String catalog = connection.getCatalog();
            if (catalog == null || catalog.isBlank()) {
                return;
            }

            statement.execute("ALTER DATABASE `" + catalog.replace("`", "``")
                    + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
        } catch (Exception exception) {
            log.debug("Could not update database charset.", exception);
        }
    }

    private void updateTableCharset(Statement statement, String table) {
        try {
            statement.execute("ALTER TABLE `" + table + "` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
        } catch (Exception exception) {
            log.debug("Could not update table charset: {}", table, exception);
        }
    }
}
