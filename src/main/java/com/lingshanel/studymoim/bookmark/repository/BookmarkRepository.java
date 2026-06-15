package com.lingshanel.studymoim.bookmark.repository;

import com.lingshanel.studymoim.bookmark.domain.Bookmark;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByUserIdAndStudyPostId(Long userId, Long postId);

    List<Bookmark> findByUserIdOrderByIdDesc(Long userId);

    void deleteByStudyPostId(Long postId);
}
