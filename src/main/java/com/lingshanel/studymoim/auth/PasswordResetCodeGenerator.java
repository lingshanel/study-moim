package com.lingshanel.studymoim.auth;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetCodeGenerator {

    private final SecureRandom secureRandom = new SecureRandom();

    public String generateCode() {
        return String.format("%06d", secureRandom.nextInt(1_000_000));
    }
}
