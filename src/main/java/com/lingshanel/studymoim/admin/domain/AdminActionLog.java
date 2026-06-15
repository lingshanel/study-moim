package com.lingshanel.studymoim.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_action_logs")
public class AdminActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String actionType;

    @Column(nullable = false, length = 40)
    private String targetType;

    private Long targetId;

    @Column(nullable = false, length = 500)
    private String summary;

    @Column(nullable = false)
    private Long adminId;

    @Column(nullable = false, length = 30)
    private String adminNickname;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected AdminActionLog() {
    }

    public AdminActionLog(String actionType, String targetType, Long targetId, String summary, UserSnapshot admin) {
        this.actionType = actionType;
        this.targetType = targetType;
        this.targetId = targetId;
        this.summary = summary;
        this.adminId = admin.id();
        this.adminNickname = admin.nickname();
    }

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getActionType() { return actionType; }
    public String getTargetType() { return targetType; }
    public Long getTargetId() { return targetId; }
    public String getSummary() { return summary; }
    public Long getAdminId() { return adminId; }
    public String getAdminNickname() { return adminNickname; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public record UserSnapshot(Long id, String nickname) {
    }
}
