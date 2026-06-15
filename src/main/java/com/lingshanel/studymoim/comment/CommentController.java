package com.lingshanel.studymoim.comment;

import com.lingshanel.studymoim.auth.CurrentUserService;
import com.lingshanel.studymoim.comment.dto.CommentCreateRequest;
import com.lingshanel.studymoim.comment.dto.CommentResponse;
import com.lingshanel.studymoim.comment.dto.CommentUpdateRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;
    private final CurrentUserService currentUserService;

    public CommentController(CommentService commentService, CurrentUserService currentUserService) {
        this.commentService = commentService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/study-posts/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest request,
            HttpSession session
    ) {
        return commentService.create(postId, request, currentUserService.getCurrentUser(session));
    }

    @GetMapping("/study-posts/{postId}/comments")
    public List<CommentResponse> getComments(@PathVariable Long postId) {
        return commentService.getComments(postId);
    }

    @PatchMapping("/comments/{commentId}")
    public CommentResponse update(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request,
            HttpSession session
    ) {
        return commentService.update(commentId, request, currentUserService.getCurrentUser(session));
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId, HttpSession session) {
        commentService.delete(commentId, currentUserService.getCurrentUser(session));
    }
}
