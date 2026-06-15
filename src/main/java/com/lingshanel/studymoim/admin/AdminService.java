package com.lingshanel.studymoim.admin;

import com.lingshanel.studymoim.admin.dto.AdminUserResponse;
import com.lingshanel.studymoim.common.error.BadRequestException;
import com.lingshanel.studymoim.bookmark.repository.BookmarkRepository;
import com.lingshanel.studymoim.comment.domain.Comment;
import com.lingshanel.studymoim.comment.repository.CommentRepository;
import com.lingshanel.studymoim.common.error.ForbiddenException;
import com.lingshanel.studymoim.common.error.NotFoundException;
import com.lingshanel.studymoim.like.repository.LikeRepository;
import com.lingshanel.studymoim.report.repository.PostReportRepository;
import com.lingshanel.studymoim.study.domain.StudyPost;
import com.lingshanel.studymoim.study.repository.StudyPostRepository;
import com.lingshanel.studymoim.user.domain.User;
import com.lingshanel.studymoim.user.domain.UserStatus;
import com.lingshanel.studymoim.user.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final StudyPostRepository studyPostRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final PostReportRepository postReportRepository;
    private final AdminActionLogService actionLogService;

    public AdminService(
            UserRepository userRepository,
            StudyPostRepository studyPostRepository,
            CommentRepository commentRepository,
            LikeRepository likeRepository,
            BookmarkRepository bookmarkRepository,
            PostReportRepository postReportRepository,
            AdminActionLogService actionLogService
    ) {
        this.userRepository = userRepository;
        this.studyPostRepository = studyPostRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.postReportRepository = postReportRepository;
        this.actionLogService = actionLogService;
    }

    @Transactional(readOnly = true)
    public List<AdminUserResponse> getUsers(String keyword, User admin) {
        validateAdmin(admin);
        return userRepository.searchForAdmin(normalize(keyword)).stream()
                .map(AdminUserResponse::from)
                .toList();
    }

    @Transactional
    public AdminUserResponse updateUserStatus(Long userId, UserStatus status, String reason, User admin) {
        validateAdmin(admin);
        User user = getUser(userId);
        if (user.isAdmin()) {
            throw new ForbiddenException("관리자 계정 상태는 변경할 수 없습니다.");
        }
        if (status == UserStatus.BANNED) {
            String normalizedReason = normalize(reason);
            if (normalizedReason == null) {
                throw new BadRequestException("회원 제재 사유를 입력해주세요.");
            }
            user.ban(normalizedReason);
            actionLogService.record("USER_BANNED", "USER", userId, user.getNickname() + " 제재: " + normalizedReason, admin);
        } else {
            user.changeStatus(status);
            actionLogService.record("USER_RESTORED", "USER", userId, user.getNickname() + " 제재 해제", admin);
        }
        return AdminUserResponse.from(user);
    }

    @Transactional
    public void deleteStudyPost(Long postId, User admin) {
        validateAdmin(admin);
        StudyPost post = studyPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("모집글을 찾을 수 없습니다."));
        String summary = post.getTitle() + " / 작성자 " + post.getUser().getNickname();
        postReportRepository.deleteByStudyPostId(postId);
        commentRepository.deleteByStudyPostId(postId);
        likeRepository.deleteByStudyPostId(postId);
        bookmarkRepository.deleteByStudyPostId(postId);
        studyPostRepository.delete(post);
        actionLogService.record("POST_DELETED", "POST", postId, summary, admin);
    }

    @Transactional
    public void deleteComment(Long commentId, User admin) {
        validateAdmin(admin);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
        String summary = comment.getContent().length() > 80
                ? comment.getContent().substring(0, 80) + "..."
                : comment.getContent();
        comment.delete();
        actionLogService.record("COMMENT_DELETED", "COMMENT", commentId, summary, admin);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));
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
