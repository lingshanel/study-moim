package com.lingshanel.studymoim.admin;

import com.lingshanel.studymoim.admin.dto.AdminUserResponse;
import com.lingshanel.studymoim.admin.dto.AdminUserStatusRequest;
import com.lingshanel.studymoim.admin.dto.AdminActionLogResponse;
import com.lingshanel.studymoim.auth.CurrentUserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final AdminActionLogService adminActionLogService;
    private final CurrentUserService currentUserService;

    public AdminController(
            AdminService adminService,
            AdminActionLogService adminActionLogService,
            CurrentUserService currentUserService
    ) {
        this.adminService = adminService;
        this.adminActionLogService = adminActionLogService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/users")
    public List<AdminUserResponse> getUsers(
            @RequestParam(required = false) String keyword,
            HttpSession session
    ) {
        return adminService.getUsers(keyword, currentUserService.getCurrentUser(session));
    }

    @GetMapping("/action-logs")
    public List<AdminActionLogResponse> getActionLogs(HttpSession session) {
        return adminActionLogService.getRecentLogs(currentUserService.getCurrentUser(session));
    }

    @PatchMapping("/users/{userId}/status")
    public AdminUserResponse updateUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody AdminUserStatusRequest request,
            HttpSession session
    ) {
        return adminService.updateUserStatus(
                userId,
                request.status(),
                request.reason(),
                currentUserService.getCurrentUser(session)
        );
    }

    @DeleteMapping("/study-posts/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudyPost(@PathVariable Long postId, HttpSession session) {
        adminService.deleteStudyPost(postId, currentUserService.getCurrentUser(session));
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId, HttpSession session) {
        adminService.deleteComment(commentId, currentUserService.getCurrentUser(session));
    }
}
