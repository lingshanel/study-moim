package com.lingshanel.studymoim.study.dto;

import com.lingshanel.studymoim.study.domain.RecruitmentStatus;
import com.lingshanel.studymoim.study.domain.StudyPost;
import com.lingshanel.studymoim.study.domain.StudyType;
import java.time.LocalDateTime;

public record StudyPostResponse(
        Long id,
        Long authorId,
        String authorNickname,
        Long categoryId,
        String categoryName,
        String categorySlug,
        String title,
        String content,
        StudyType studyType,
        String location,
        String imageUrl,
        RecruitmentStatus recruitmentStatus,
        Integer maxMembers,
        long viewCount,
        long likeCount,
        long bookmarkCount,
        long commentCount,
        boolean likedByMe,
        boolean bookmarkedByMe,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static StudyPostResponse from(StudyPost post, long commentCount, boolean likedByMe, boolean bookmarkedByMe) {
        return new StudyPostResponse(
                post.getId(),
                post.getUser().getId(),
                post.getUser().getNickname(),
                post.getCategory().getId(),
                post.getCategory().getName(),
                post.getCategory().getSlug(),
                post.getTitle(),
                post.getContent(),
                post.getStudyType(),
                post.getLocation(),
                post.getImageUrl(),
                post.getRecruitmentStatus(),
                post.getMaxMembers(),
                post.getViewCount(),
                post.getLikeCount(),
                post.getBookmarkCount(),
                commentCount,
                likedByMe,
                bookmarkedByMe,
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
