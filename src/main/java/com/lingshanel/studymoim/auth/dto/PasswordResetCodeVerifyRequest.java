package com.lingshanel.studymoim.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordResetCodeVerifyRequest(
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "이메일을 입력해주세요.")
        String email,

        @NotBlank(message = "닉네임을 입력해주세요.")
        String nickname,

        @Pattern(regexp = "\\d{6}", message = "인증번호 6자리를 입력해주세요.")
        String code
) {
}
