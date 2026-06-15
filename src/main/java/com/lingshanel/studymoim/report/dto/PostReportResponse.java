package com.lingshanel.studymoim.report.dto;

import com.lingshanel.studymoim.report.domain.PostReport;
import com.lingshanel.studymoim.report.domain.ReportStatus;
import com.lingshanel.studymoim.report.domain.ReportTargetType;
import java.time.LocalDateTime;

public record PostReportResponse(
        Long id,
        ReportTargetType targetType,
        Long postId,
        String postTitle,
        Long commentId,
        String commentContent,
        Long reporterId,
        String reporterNickname,
        String reason,
        ReportStatus status,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt
) {
    public static PostReportResponse from(PostReport report) {
        return new PostReportResponse(
                report.getId(),
                report.getTargetType(),
                report.getStudyPost().getId(),
                report.getStudyPost().getTitle(),
                report.getComment() == null ? null : report.getComment().getId(),
                report.getComment() == null ? null : report.getComment().getContent(),
                report.getReporter().getId(),
                report.getReporter().getNickname(),
                report.getReason(),
                report.getStatus(),
                report.getCreatedAt(),
                report.getResolvedAt()
        );
    }
}
