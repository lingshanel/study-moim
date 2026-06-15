package com.lingshanel.studymoim.report.domain;

import com.lingshanel.studymoim.comment.domain.Comment;
import com.lingshanel.studymoim.study.domain.StudyPost;
import com.lingshanel.studymoim.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_reports")
public class PostReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_post_id", nullable = false)
    private StudyPost studyPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReportTargetType targetType;

    @Column(nullable = false, length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReportStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime resolvedAt;

    protected PostReport() {
    }

    public PostReport(StudyPost studyPost, User reporter, String reason) {
        this.studyPost = studyPost;
        this.comment = null;
        this.reporter = reporter;
        this.targetType = ReportTargetType.POST;
        this.reason = reason;
        this.status = ReportStatus.PENDING;
    }

    public PostReport(Comment comment, User reporter, String reason) {
        this.studyPost = comment.getStudyPost();
        this.comment = comment;
        this.reporter = reporter;
        this.targetType = ReportTargetType.COMMENT;
        this.reason = reason;
        this.status = ReportStatus.PENDING;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void resolve() {
        this.status = ReportStatus.RESOLVED;
        this.resolvedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public StudyPost getStudyPost() {
        return studyPost;
    }

    public User getReporter() {
        return reporter;
    }

    public Comment getComment() {
        return comment;
    }

    public ReportTargetType getTargetType() {
        return targetType;
    }

    public String getReason() {
        return reason;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }
}
