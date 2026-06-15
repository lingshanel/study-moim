package com.lingshanel.studymoim.comment.repository;

import com.lingshanel.studymoim.comment.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByStudyPostIdOrderByCreatedAtAsc(Long postId);

    long countByStudyPostIdAndDeletedFalse(Long postId);

    void deleteByStudyPostId(Long postId);
}
