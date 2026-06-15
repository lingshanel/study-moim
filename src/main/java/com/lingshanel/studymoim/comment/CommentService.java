package com.lingshanel.studymoim.comment;

import com.lingshanel.studymoim.comment.domain.Comment;
import com.lingshanel.studymoim.comment.dto.CommentCreateRequest;
import com.lingshanel.studymoim.comment.dto.CommentResponse;
import com.lingshanel.studymoim.comment.dto.CommentUpdateRequest;
import com.lingshanel.studymoim.comment.repository.CommentRepository;
import com.lingshanel.studymoim.common.error.ForbiddenException;
import com.lingshanel.studymoim.common.error.NotFoundException;
import com.lingshanel.studymoim.study.domain.StudyPost;
import com.lingshanel.studymoim.study.repository.StudyPostRepository;
import com.lingshanel.studymoim.user.domain.User;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final StudyPostRepository studyPostRepository;

    public CommentService(CommentRepository commentRepository, StudyPostRepository studyPostRepository) {
        this.commentRepository = commentRepository;
        this.studyPostRepository = studyPostRepository;
    }

    @Transactional
    public CommentResponse create(Long postId, CommentCreateRequest request, User user) {
        StudyPost post = studyPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("모집글을 찾을 수 없습니다."));
        Comment parentComment = getParentComment(request.parentCommentId(), postId);
        Comment comment = new Comment(post, user, parentComment, request.content());
        return CommentResponse.from(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId) {
        if (!studyPostRepository.existsById(postId)) {
            throw new NotFoundException("모집글을 찾을 수 없습니다.");
        }

        return commentRepository.findByStudyPostIdOrderByCreatedAtAsc(postId).stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public CommentResponse update(Long commentId, CommentUpdateRequest request, User user) {
        Comment comment = getComment(commentId);
        validateWriter(comment, user);
        comment.update(request.content());
        return CommentResponse.from(comment);
    }

    @Transactional
    public void delete(Long commentId, User user) {
        Comment comment = getComment(commentId);
        validateWriter(comment, user);
        comment.delete();
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
    }

    private Comment getParentComment(Long parentCommentId, Long postId) {
        if (parentCommentId == null) {
            return null;
        }

        Comment parentComment = getComment(parentCommentId);
        if (!parentComment.getStudyPost().getId().equals(postId)) {
            throw new NotFoundException("부모 댓글을 찾을 수 없습니다.");
        }
        return parentComment;
    }

    private void validateWriter(Comment comment, User user) {
        if (!comment.isWrittenBy(user)) {
            throw new ForbiddenException("작성자만 수정하거나 삭제할 수 있습니다.");
        }
    }
}
