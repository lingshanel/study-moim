package com.lingshanel.studymoim.admin.dto;

import com.lingshanel.studymoim.user.domain.User;
import com.lingshanel.studymoim.user.domain.UserRole;
import com.lingshanel.studymoim.user.domain.UserStatus;
import java.time.LocalDateTime;

public record AdminUserResponse(
        Long id,
        String email,
        String nickname,
        UserRole role,
        UserStatus status,
        String banReason,
        LocalDateTime createdAt
) {
    public static AdminUserResponse from(User user) {
        return new AdminUserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole(),
                user.getStatus(),
                user.getBanReason(),
                user.getCreatedAt()
        );
    }
}
