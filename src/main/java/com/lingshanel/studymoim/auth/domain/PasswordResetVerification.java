package com.lingshanel.studymoim.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_verifications")
public class PasswordResetVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false)
    private String codeHash;

    @Column(nullable = false)
    private LocalDateTime codeExpiresAt;

    private String resetTokenHash;

    private LocalDateTime resetTokenExpiresAt;

    private LocalDateTime verifiedAt;

    private LocalDateTime consumedAt;

    private Integer failedCodeAttempts;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected PasswordResetVerification() {
    }

    public PasswordResetVerification(String email, String nickname, String codeHash, LocalDateTime codeExpiresAt) {
        this.email = email;
        this.nickname = nickname;
        this.codeHash = codeHash;
        this.codeExpiresAt = codeExpiresAt;
        this.failedCodeAttempts = 0;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void verify(String resetTokenHash, LocalDateTime resetTokenExpiresAt) {
        this.verifiedAt = LocalDateTime.now();
        this.resetTokenHash = resetTokenHash;
        this.resetTokenExpiresAt = resetTokenExpiresAt;
    }

    public void consume() {
        this.consumedAt = LocalDateTime.now();
    }

    public int getFailedCodeAttempts() {
        return failedCodeAttempts == null ? 0 : failedCodeAttempts;
    }

    public void registerCodeFailure() {
        this.failedCodeAttempts = getFailedCodeAttempts() + 1;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getNickname() { return nickname; }
    public String getCodeHash() { return codeHash; }
    public LocalDateTime getCodeExpiresAt() { return codeExpiresAt; }
    public String getResetTokenHash() { return resetTokenHash; }
    public LocalDateTime getResetTokenExpiresAt() { return resetTokenExpiresAt; }
    public LocalDateTime getVerifiedAt() { return verifiedAt; }
    public LocalDateTime getConsumedAt() { return consumedAt; }
}
