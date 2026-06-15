package com.lingshanel.studymoim.admin.dto;

import com.lingshanel.studymoim.admin.domain.AdminActionLog;
import java.time.LocalDateTime;

public record AdminActionLogResponse(
        Long id,
        String actionType,
        String targetType,
        Long targetId,
        String summary,
        Long adminId,
        String adminNickname,
        LocalDateTime createdAt
) {
    public static AdminActionLogResponse from(AdminActionLog log) {
        return new AdminActionLogResponse(
                log.getId(),
                log.getActionType(),
                log.getTargetType(),
                log.getTargetId(),
                log.getSummary(),
                log.getAdminId(),
                log.getAdminNickname(),
                log.getCreatedAt()
        );
    }
}
