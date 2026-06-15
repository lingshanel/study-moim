package com.lingshanel.studymoim.report;

import com.lingshanel.studymoim.auth.CurrentUserService;
import com.lingshanel.studymoim.report.domain.ReportStatus;
import com.lingshanel.studymoim.report.dto.PostReportCreateRequest;
import com.lingshanel.studymoim.report.dto.PostReportResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    private final ReportService reportService;
    private final CurrentUserService currentUserService;

    public ReportController(ReportService reportService, CurrentUserService currentUserService) {
        this.reportService = reportService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/api/study-posts/{postId}/reports")
    public PostReportResponse reportPost(
            @PathVariable Long postId,
            @Valid @RequestBody PostReportCreateRequest request,
            HttpSession session
    ) {
        return reportService.reportPost(postId, request, currentUserService.getCurrentUser(session));
    }

    @PostMapping("/api/comments/{commentId}/reports")
    public PostReportResponse reportComment(
            @PathVariable Long commentId,
            @Valid @RequestBody PostReportCreateRequest request,
            HttpSession session
    ) {
        return reportService.reportComment(commentId, request, currentUserService.getCurrentUser(session));
    }

    @GetMapping("/api/admin/reports")
    public List<PostReportResponse> getReports(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ReportStatus status,
            HttpSession session
    ) {
        return reportService.getReports(keyword, status, currentUserService.getCurrentUser(session));
    }

    @PatchMapping("/api/admin/reports/{reportId}/resolve")
    public PostReportResponse resolveReport(@PathVariable Long reportId, HttpSession session) {
        return reportService.resolveReport(reportId, currentUserService.getCurrentUser(session));
    }
}
