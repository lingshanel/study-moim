package com.lingshanel.studymoim.admin.dto;

import com.lingshanel.studymoim.user.domain.UserStatus;
import jakarta.validation.constraints.NotNull;

public record AdminUserStatusRequest(
        @NotNull(message = "변경할 회원 상태를 선택해주세요.")
        UserStatus status,
        String reason
) {
}
