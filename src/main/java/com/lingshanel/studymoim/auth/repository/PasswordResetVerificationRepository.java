package com.lingshanel.studymoim.auth.repository;

import com.lingshanel.studymoim.auth.domain.PasswordResetVerification;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetVerificationRepository extends JpaRepository<PasswordResetVerification, Long> {
    Optional<PasswordResetVerification> findTopByEmailAndNicknameOrderByIdDesc(String email, String nickname);
}
