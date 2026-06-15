package com.lingshanel.studymoim.reaction;

import com.lingshanel.studymoim.bookmark.domain.Bookmark;
import com.lingshanel.studymoim.bookmark.repository.BookmarkRepository;
import com.lingshanel.studymoim.common.error.NotFoundException;
import com.lingshanel.studymoim.like.domain.Like;
import com.lingshanel.studymoim.like.repository.LikeRepository;
import com.lingshanel.studymoim.study.domain.StudyPost;
import com.lingshanel.studymoim.study.repository.StudyPostRepository;
import com.lingshanel.studymoim.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReactionService {

    private final StudyPostRepository studyPostRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;

    public ReactionService(
            StudyPostRepository studyPostRepository,
            LikeRepository likeRepository,
            BookmarkRepository bookmarkRepository
    ) {
        this.studyPostRepository = studyPostRepository;
        this.likeRepository = likeRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    @Transactional
    public void like(Long postId, User user) {
        StudyPost post = getPost(postId);
        if (likeRepository.findByUserIdAndStudyPostId(user.getId(), postId).isPresent()) {
            return;
        }
        likeRepository.save(new Like(user, post));
        post.increaseLikeCount();
    }

    @Transactional
    public void unlike(Long postId, User user) {
        StudyPost post = getPost(postId);
        likeRepository.findByUserIdAndStudyPostId(user.getId(), postId).ifPresent(like -> {
            likeRepository.delete(like);
            post.decreaseLikeCount();
        });
    }

    @Transactional
    public void bookmark(Long postId, User user) {
        StudyPost post = getPost(postId);
        if (bookmarkRepository.findByUserIdAndStudyPostId(user.getId(), postId).isPresent()) {
            return;
        }
        bookmarkRepository.save(new Bookmark(user, post));
        post.increaseBookmarkCount();
    }

    @Transactional
    public void unbookmark(Long postId, User user) {
        StudyPost post = getPost(postId);
        bookmarkRepository.findByUserIdAndStudyPostId(user.getId(), postId).ifPresent(bookmark -> {
            bookmarkRepository.delete(bookmark);
            post.decreaseBookmarkCount();
        });
    }

    private StudyPost getPost(Long postId) {
        return studyPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("모집글을 찾을 수 없습니다."));
    }
}
