package com.lingshanel.studymoim.study;

import com.lingshanel.studymoim.bookmark.repository.BookmarkRepository;
import com.lingshanel.studymoim.category.domain.Category;
import com.lingshanel.studymoim.category.repository.CategoryRepository;
import com.lingshanel.studymoim.comment.repository.CommentRepository;
import com.lingshanel.studymoim.common.error.BadRequestException;
import com.lingshanel.studymoim.common.error.ForbiddenException;
import com.lingshanel.studymoim.common.error.NotFoundException;
import com.lingshanel.studymoim.common.file.LocalFileStorageService;
import com.lingshanel.studymoim.like.repository.LikeRepository;
import com.lingshanel.studymoim.study.domain.RecruitmentStatus;
import com.lingshanel.studymoim.study.domain.StudyPost;
import com.lingshanel.studymoim.study.domain.StudyType;
import com.lingshanel.studymoim.study.dto.StudyPostCreateRequest;
import com.lingshanel.studymoim.study.dto.StudyPostResponse;
import com.lingshanel.studymoim.study.dto.StudyPostStatusRequest;
import com.lingshanel.studymoim.study.dto.StudyPostUpdateRequest;
import com.lingshanel.studymoim.study.repository.StudyPostRepository;
import com.lingshanel.studymoim.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StudyPostService {

    private final StudyPostRepository studyPostRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final LocalFileStorageService localFileStorageService;

    public StudyPostService(
            StudyPostRepository studyPostRepository,
            CategoryRepository categoryRepository,
            CommentRepository commentRepository,
            LikeRepository likeRepository,
            BookmarkRepository bookmarkRepository,
            LocalFileStorageService localFileStorageService
    ) {
        this.studyPostRepository = studyPostRepository;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.localFileStorageService = localFileStorageService;
    }

    @Transactional
    public StudyPostResponse create(StudyPostCreateRequest request, User user) {
        Category category = getCategory(request.categorySlug());
        StudyPost post = new StudyPost(
                user,
                category,
                request.title(),
                request.content(),
                request.studyType(),
                request.location(),
                request.maxMembers()
        );

        return toResponse(studyPostRepository.save(post), user.getId());
    }

    @Transactional(readOnly = true)
    public Page<StudyPostResponse> search(
            String category,
            String keyword,
            Long authorId,
            String studyType,
            String status,
            String sort,
            int page,
            int size,
            Long viewerId
    ) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                sortBy(sort)
        );

        String categoryFilter = StringUtils.hasText(category) ? category : null;
        String keywordFilter = StringUtils.hasText(keyword) ? keyword : null;
        StudyType studyTypeFilter = parseStudyType(studyType);
        RecruitmentStatus statusFilter = parseStatus(status);

        return studyPostRepository.search(categoryFilter, keywordFilter, authorId, studyTypeFilter, statusFilter, pageable)
                .map(post -> toResponse(post, viewerId));
    }

    @Transactional
    public StudyPostResponse get(Long postId, Long viewerId, boolean increaseView) {
        StudyPost post = getPost(postId);
        if (increaseView) {
            post.increaseViewCount();
        }
        return toResponse(post, viewerId);
    }

    @Transactional
    public StudyPostResponse update(Long postId, StudyPostUpdateRequest request, User user) {
        StudyPost post = getPost(postId);
        validateWriter(post, user);
        Category category = getCategory(request.categorySlug());
        post.update(category, request.title(), request.content(), request.studyType(), request.location(), request.maxMembers());
        return toResponse(post, user.getId());
    }

    @Transactional
    public void delete(Long postId, User user) {
        StudyPost post = getPost(postId);
        validateWriter(post, user);
        commentRepository.deleteByStudyPostId(postId);
        likeRepository.deleteByStudyPostId(postId);
        bookmarkRepository.deleteByStudyPostId(postId);
        studyPostRepository.delete(post);
    }

    @Transactional
    public StudyPostResponse updateStatus(Long postId, StudyPostStatusRequest request, User user) {
        StudyPost post = getPost(postId);
        validateWriter(post, user);
        post.updateStatus(request.recruitmentStatus());
        return toResponse(post, user.getId());
    }

    @Transactional
    public StudyPostResponse uploadImage(Long postId, MultipartFile image, User user) {
        StudyPost post = getPost(postId);
        validateWriter(post, user);
        String imageUrl = localFileStorageService.storeStudyPostImage(image);
        post.updateImageUrl(imageUrl);
        return toResponse(post, user.getId());
    }

    private StudyPost getPost(Long postId) {
        return studyPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("모집글을 찾을 수 없습니다."));
    }

    private StudyPostResponse toResponse(StudyPost post, Long viewerId) {
        long commentCount = commentRepository.countByStudyPostIdAndDeletedFalse(post.getId());
        boolean likedByMe = viewerId != null && likeRepository.findByUserIdAndStudyPostId(viewerId, post.getId()).isPresent();
        boolean bookmarkedByMe = viewerId != null && bookmarkRepository.findByUserIdAndStudyPostId(viewerId, post.getId()).isPresent();
        return StudyPostResponse.from(post, commentCount, likedByMe, bookmarkedByMe);
    }

    private Category getCategory(String categorySlug) {
        return categoryRepository.findBySlug(categorySlug)
                .orElseThrow(() -> new NotFoundException("카테고리를 찾을 수 없습니다."));
    }

    private void validateWriter(StudyPost post, User user) {
        if (!post.isWrittenBy(user)) {
            throw new ForbiddenException("작성자만 수정하거나 삭제할 수 있습니다.");
        }
    }

    private Sort sortBy(String sort) {
        if ("popular".equalsIgnoreCase(sort)) {
            return Sort.by(Sort.Direction.DESC, "likeCount")
                    .and(Sort.by(Sort.Direction.DESC, "createdAt"));
        }
        if ("bookmarked".equalsIgnoreCase(sort)) {
            return Sort.by(Sort.Direction.DESC, "bookmarkCount")
                    .and(Sort.by(Sort.Direction.DESC, "createdAt"));
        }
        return Sort.by(Sort.Direction.DESC, "createdAt");
    }

    private StudyType parseStudyType(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return StudyType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("지원하지 않는 진행 방식입니다.");
        }
    }

    private RecruitmentStatus parseStatus(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return RecruitmentStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("지원하지 않는 모집 상태입니다.");
        }
    }
}
