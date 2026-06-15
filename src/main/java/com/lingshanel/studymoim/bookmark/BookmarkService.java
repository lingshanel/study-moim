package com.lingshanel.studymoim.bookmark;

import com.lingshanel.studymoim.bookmark.repository.BookmarkRepository;
import com.lingshanel.studymoim.comment.repository.CommentRepository;
import com.lingshanel.studymoim.study.dto.StudyPostResponse;
import com.lingshanel.studymoim.user.domain.User;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final CommentRepository commentRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, CommentRepository commentRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = true)
    public List<StudyPostResponse> getMyBookmarks(User user) {
        return bookmarkRepository.findByUserIdOrderByIdDesc(user.getId()).stream()
                .map(bookmark -> {
                    long commentCount = commentRepository.countByStudyPostIdAndDeletedFalse(bookmark.getStudyPost().getId());
                    return StudyPostResponse.from(bookmark.getStudyPost(), commentCount, false, true);
                })
                .toList();
    }
}
