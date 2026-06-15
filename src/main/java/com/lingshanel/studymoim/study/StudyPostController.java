package com.lingshanel.studymoim.study;

import com.lingshanel.studymoim.auth.AuthSession;
import com.lingshanel.studymoim.auth.CurrentUserService;
import com.lingshanel.studymoim.common.dto.PageResponse;
import com.lingshanel.studymoim.study.dto.StudyPostCreateRequest;
import com.lingshanel.studymoim.study.dto.StudyPostResponse;
import com.lingshanel.studymoim.study.dto.StudyPostStatusRequest;
import com.lingshanel.studymoim.study.dto.StudyPostUpdateRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/study-posts")
public class StudyPostController {

    private static final String VIEWED_POST_IDS = "VIEWED_STUDY_POST_IDS";

    private final StudyPostService studyPostService;
    private final CurrentUserService currentUserService;

    public StudyPostController(StudyPostService studyPostService, CurrentUserService currentUserService) {
        this.studyPostService = studyPostService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudyPostResponse create(
            @Valid @RequestBody StudyPostCreateRequest request,
            HttpSession session
    ) {
        return studyPostService.create(request, currentUserService.getCurrentUser(session));
    }

    @GetMapping
    public PageResponse<StudyPostResponse> search(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String studyType,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session
    ) {
        return PageResponse.from(studyPostService.search(category, keyword, authorId, studyType, status, sort, page, size, currentUserId(session)));
    }

    @GetMapping("/{postId}")
    public StudyPostResponse get(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "true") boolean increaseView,
            HttpSession session
    ) {
        return studyPostService.get(postId, currentUserId(session), shouldIncreaseView(session, postId, increaseView));
    }

    @PatchMapping("/{postId}")
    public StudyPostResponse update(
            @PathVariable Long postId,
            @Valid @RequestBody StudyPostUpdateRequest request,
            HttpSession session
    ) {
        return studyPostService.update(postId, request, currentUserService.getCurrentUser(session));
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long postId, HttpSession session) {
        studyPostService.delete(postId, currentUserService.getCurrentUser(session));
    }

    @PatchMapping("/{postId}/status")
    public StudyPostResponse updateStatus(
            @PathVariable Long postId,
            @Valid @RequestBody StudyPostStatusRequest request,
            HttpSession session
    ) {
        return studyPostService.updateStatus(postId, request, currentUserService.getCurrentUser(session));
    }

    @PostMapping("/{postId}/image")
    public StudyPostResponse uploadImage(
            @PathVariable Long postId,
            @RequestParam MultipartFile image,
            HttpSession session
    ) {
        return studyPostService.uploadImage(postId, image, currentUserService.getCurrentUser(session));
    }

    private Long currentUserId(HttpSession session) {
        Object userId = session.getAttribute(AuthSession.LOGIN_USER_ID);
        return userId instanceof Long id ? id : null;
    }

    @SuppressWarnings("unchecked")
    private boolean shouldIncreaseView(HttpSession session, Long postId, boolean increaseView) {
        if (!increaseView) {
            return false;
        }

        Object viewedPostIds = session.getAttribute(VIEWED_POST_IDS);
        Set<Long> viewedIds = viewedPostIds instanceof Set<?> ids
                ? (Set<Long>) ids
                : new HashSet<>();

        if (viewedIds.contains(postId)) {
            return false;
        }

        viewedIds.add(postId);
        session.setAttribute(VIEWED_POST_IDS, viewedIds);
        return true;
    }
}
