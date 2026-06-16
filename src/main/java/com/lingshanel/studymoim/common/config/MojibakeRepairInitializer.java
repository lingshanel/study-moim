package com.lingshanel.studymoim.common.config;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class MojibakeRepairInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MojibakeRepairInitializer.class);

    private final JdbcTemplate jdbcTemplate;

    public MojibakeRepairInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        repair("categories", "name");
        repair("study_posts", "title", "content", "location");
        repair("comments", "content");
        repair("post_reports", "reason");
        repair("users", "nickname", "ban_reason");
        repair("admin_action_logs", "summary", "admin_nickname");
    }

    private void repair(String table, String... columns) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(selectSql(table, columns));
            for (Map<String, Object> row : rows) {
                Long id = ((Number) row.get("id")).longValue();
                for (String column : columns) {
                    Object value = row.get(column);
                    if (value instanceof String text) {
                        repairColumn(table, column, id, text);
                    }
                }
            }
        } catch (Exception exception) {
            log.debug("Could not repair mojibake text for table: {}", table, exception);
        }
    }

    private String selectSql(String table, String[] columns) {
        StringBuilder builder = new StringBuilder("SELECT id");
        for (String column : columns) {
            builder.append(", `").append(column).append("`");
        }
        builder.append(" FROM `").append(table).append("`");
        return builder.toString();
    }

    private void repairColumn(String table, String column, Long id, String text) {
        String decoded = decodeMojibake(text);
        if (decoded.equals(text) || !looksRecovered(decoded)) {
            return;
        }

        jdbcTemplate.update("UPDATE `" + table + "` SET `" + column + "` = ? WHERE id = ?", decoded, id);
    }

    private String decodeMojibake(String text) {
        try {
            return new String(text.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        } catch (Exception exception) {
            return text;
        }
    }

    private boolean looksRecovered(String text) {
        return text.codePoints().anyMatch(this::isKorean);
    }

    private boolean isKorean(int codePoint) {
        return (codePoint >= 0xAC00 && codePoint <= 0xD7A3)
                || (codePoint >= 0x3130 && codePoint <= 0x318F)
                || (codePoint >= 0x1100 && codePoint <= 0x11FF);
    }
}
