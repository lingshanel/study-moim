package com.lingshanel.studymoim.like.repository;

import com.lingshanel.studymoim.like.domain.Like;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndStudyPostId(Long userId, Long postId);

    void deleteByStudyPostId(Long postId);
}
