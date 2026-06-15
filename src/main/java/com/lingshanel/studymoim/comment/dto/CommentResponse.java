package com.lingshanel.studymoim.comment.dto;

import com.lingshanel.studymoim.comment.domain.Comment;
import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long postId,
        Long authorId,
        String authorNickname,
        Long parentCommentId,
        String content,
        boolean deleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getStudyPost().getId(),
                comment.getUser().getId(),
                comment.getUser().getNickname(),
                comment.getParentComment() == null ? null : comment.getParentComment().getId(),
                comment.isDeleted() ? "삭제된 댓글입니다." : comment.getContent(),
                comment.isDeleted(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
