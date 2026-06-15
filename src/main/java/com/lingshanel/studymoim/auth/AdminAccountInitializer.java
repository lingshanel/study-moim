package com.lingshanel.studymoim.auth;

import com.lingshanel.studymoim.user.domain.User;
import com.lingshanel.studymoim.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@ConditionalOnProperty(prefix = "study-moim.admin", name = "enabled", havingValue = "true")
public class AdminAccountInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String email;
    private final String nickname;
    private final String password;

    public AdminAccountInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${study-moim.admin.email:admin@study-moim.local}") String email,
            @Value("${study-moim.admin.nickname:superadmin}") String nickname,
            @Value("${study-moim.admin.password:}") String password
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    @Override
    public void run(String... args) {
        if (password.isBlank()) {
            throw new IllegalStateException("관리자 계정 생성을 활성화하려면 ADMIN_PASSWORD를 설정해야 합니다.");
        }
        userRepository.findByEmail(email)
                .or(() -> userRepository.findByNickname(nickname))
                .orElseGet(() -> userRepository.save(User.admin(
                        email,
                        passwordEncoder.encode(password),
                        nickname
                )));
    }
}
