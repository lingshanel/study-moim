package com.lingshanel.studymoim.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetCodeSender {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetCodeSender.class);

    private final JavaMailSender mailSender;
    private final boolean mailEnabled;
    private final String from;

    public PasswordResetCodeSender(
            JavaMailSender mailSender,
            @Value("${study-moim.mail.enabled:false}") boolean mailEnabled,
            @Value("${study-moim.mail.from:no-reply@study-moim.local}") String from
    ) {
        this.mailSender = mailSender;
        this.mailEnabled = mailEnabled;
        this.from = from;
    }

    public void send(String email, String code) {
        if (!mailEnabled) {
            log.info("Password reset verification code for {}: {}", email, code);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("[스터디모임] 비밀번호 재설정 인증번호");
        message.setText("인증번호는 " + code + " 입니다. 5분 안에 입력해주세요.");
        mailSender.send(message);
    }
}
