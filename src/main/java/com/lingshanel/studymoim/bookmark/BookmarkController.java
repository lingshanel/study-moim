package com.lingshanel.studymoim.bookmark;

import com.lingshanel.studymoim.auth.CurrentUserService;
import com.lingshanel.studymoim.study.dto.StudyPostResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final CurrentUserService currentUserService;

    public BookmarkController(BookmarkService bookmarkService, CurrentUserService currentUserService) {
        this.bookmarkService = bookmarkService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public List<StudyPostResponse> getMyBookmarks(HttpSession session) {
        return bookmarkService.getMyBookmarks(currentUserService.getCurrentUser(session));
    }
}
