package com.lingshanel.studymoim.reaction;

import com.lingshanel.studymoim.auth.CurrentUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/study-posts/{postId}")
public class ReactionController {

    private final ReactionService reactionService;
    private final CurrentUserService currentUserService;

    public ReactionController(ReactionService reactionService, CurrentUserService currentUserService) {
        this.reactionService = reactionService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void like(@PathVariable Long postId, HttpSession session) {
        reactionService.like(postId, currentUserService.getCurrentUser(session));
    }

    @DeleteMapping("/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlike(@PathVariable Long postId, HttpSession session) {
        reactionService.unlike(postId, currentUserService.getCurrentUser(session));
    }

    @PostMapping("/bookmarks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void bookmark(@PathVariable Long postId, HttpSession session) {
        reactionService.bookmark(postId, currentUserService.getCurrentUser(session));
    }

    @DeleteMapping("/bookmarks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unbookmark(@PathVariable Long postId, HttpSession session) {
        reactionService.unbookmark(postId, currentUserService.getCurrentUser(session));
    }
}
