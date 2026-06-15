package com.lingshanel.studymoim.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 30)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status;

    @Column(length = 300)
    private String banReason;

    private Integer failedLoginAttempts;

    private LocalDateTime loginLockedUntil;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected User() {
    }

    public User(String email, String password, String nickname) {
        this(email, password, nickname, UserRole.USER);
    }

    private User(String email, String password, String nickname, UserRole role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.status = UserStatus.ACTIVE;
        this.failedLoginAttempts = 0;
    }

    public static User admin(String email, String password, String nickname) {
        return new User(email, password, nickname, UserRole.ADMIN);
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public UserRole getRole() {
        return role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public String getBanReason() {
        return banReason;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts == null ? 0 : failedLoginAttempts;
    }

    public LocalDateTime getLoginLockedUntil() {
        return loginLockedUntil;
    }

    public boolean isLoginLocked(LocalDateTime now) {
        return loginLockedUntil != null && loginLockedUntil.isAfter(now);
    }

    public void registerLoginFailure(int maximumAttempts, LocalDateTime lockedUntil) {
        this.failedLoginAttempts = getFailedLoginAttempts() + 1;
        if (this.failedLoginAttempts >= maximumAttempts) {
            this.loginLockedUntil = lockedUntil;
            this.failedLoginAttempts = 0;
        }
    }

    public void resetLoginFailures() {
        this.failedLoginAttempts = 0;
        this.loginLockedUntil = null;
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void delete() {
        this.status = UserStatus.DELETED;
    }

    public void changeStatus(UserStatus status) {
        this.status = status;
        if (status != UserStatus.BANNED) {
            this.banReason = null;
        }
    }

    public void ban(String reason) {
        this.status = UserStatus.BANNED;
        this.banReason = reason;
    }
}
