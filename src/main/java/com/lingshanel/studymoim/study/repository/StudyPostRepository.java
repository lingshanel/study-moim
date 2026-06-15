package com.lingshanel.studymoim.study.repository;

import com.lingshanel.studymoim.study.domain.StudyPost;
import com.lingshanel.studymoim.study.domain.StudyType;
import com.lingshanel.studymoim.study.domain.RecruitmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyPostRepository extends JpaRepository<StudyPost, Long> {

    boolean existsByTitle(String title);

    @Query("""
            select post
            from StudyPost post
            join post.category category
            where (:category is null or category.slug = :category)
              and (:authorId is null or post.user.id = :authorId)
              and (:studyType is null or post.studyType = :studyType)
              and (:status is null or post.recruitmentStatus = :status)
              and (:keyword is null or lower(post.title) like lower(concat('%', :keyword, '%'))
                   or lower(post.content) like lower(concat('%', :keyword, '%')))
            """)
    Page<StudyPost> search(
            @Param("category") String category,
            @Param("keyword") String keyword,
            @Param("authorId") Long authorId,
            @Param("studyType") StudyType studyType,
            @Param("status") RecruitmentStatus status,
            Pageable pageable
    );
}
