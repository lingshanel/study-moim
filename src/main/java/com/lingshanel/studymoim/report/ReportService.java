package com.lingshanel.studymoim.report;

import com.lingshanel.studymoim.admin.AdminActionLogService;
import com.lingshanel.studymoim.comment.domain.Comment;
import com.lingshanel.studymoim.comment.repository.CommentRepository;
import com.lingshanel.studymoim.common.error.BadRequestException;
import com.lingshanel.studymoim.common.error.ForbiddenException;
import com.lingshanel.studymoim.common.error.NotFoundException;
import com.lingshanel.studymoim.report.domain.PostReport;
import com.lingshanel.studymoim.report.domain.ReportStatus;
import com.lingshanel.studymoim.report.dto.PostReportCreateRequest;
import com.lingshanel.studymoim.report.dto.PostReportResponse;
import com.lingshanel.studymoim.report.repository.PostReportRepository;
import com.lingshanel.studymoim.study.domain.StudyPost;
import com.lingshanel.studymoim.study.repository.StudyPostRepository;
import com.lingshanel.studymoim.user.domain.User;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final PostReportRepository postReportRepository;
    private final StudyPostRepository studyPostRepository;
    private final CommentRepository commentRepository;
    private final AdminActionLogService actionLogService;

    public ReportService(
            PostReportRepository postReportRepository,
            StudyPostRepository studyPostRepository,
            CommentRepository commentRepository,
            AdminActionLogService actionLogService
    ) {
        this.postReportRepository = postReportRepository;
        this.studyPostRepository = studyPostRepository;
        this.commentRepository = commentRepository;
        this.actionLogService = actionLogService;
    }

    @Transactional
    public PostReportResponse reportPost(Long postId, PostReportCreateRequest request, User reporter) {
        StudyPost post = studyPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("모집글을 찾을 수 없습니다."));
        if (post.isWrittenBy(reporter)) {
            throw new BadRequestException("본인이 작성한 모집글은 신고할 수 없습니다.");
        }
        if (postReportRepository.existsByStudyPostIdAndCommentIsNullAndReporterIdAndStatus(postId, reporter.getId(), ReportStatus.PENDING)) {
            throw new BadRequestException("이미 접수된 신고가 있습니다.");
        }
        return PostReportResponse.from(postReportRepository.save(new PostReport(post, reporter, request.reason())));
    }

    @Transactional
    public PostReportResponse reportComment(Long commentId, PostReportCreateRequest request, User reporter) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
        if (comment.isDeleted()) {
            throw new BadRequestException("삭제된 댓글은 신고할 수 없습니다.");
        }
        if (comment.isWrittenBy(reporter)) {
            throw new BadRequestException("본인이 작성한 댓글은 신고할 수 없습니다.");
        }
        if (postReportRepository.existsByCommentIdAndReporterIdAndStatus(commentId, reporter.getId(), ReportStatus.PENDING)) {
            throw new BadRequestException("이미 접수된 신고가 있습니다.");
        }
        return PostReportResponse.from(postReportRepository.save(new PostReport(comment, reporter, request.reason())));
    }

    @Transactional(readOnly = true)
    public List<PostReportResponse> getReports(String keyword, ReportStatus status, User admin) {
        validateAdmin(admin);
        return postReportRepository.searchForAdmin(normalize(keyword), status).stream()
                .map(PostReportResponse::from)
                .toList();
    }

    @Transactional
    public PostReportResponse resolveReport(Long reportId, User admin) {
        validateAdmin(admin);
        PostReport report = postReportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("신고 내역을 찾을 수 없습니다."));
        report.resolve();
        actionLogService.record(
                "REPORT_RESOLVED",
                report.getComment() == null ? "POST_REPORT" : "COMMENT_REPORT",
                reportId,
                report.getReason(),
                admin
        );
        return PostReportResponse.from(report);
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private void validateAdmin(User user) {
        if (!user.isAdmin()) {
            throw new ForbiddenException("관리자 권한이 필요합니다.");
        }
    }
}
