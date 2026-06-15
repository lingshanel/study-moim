package com.lingshanel.studymoim.report.repository;

import com.lingshanel.studymoim.report.domain.PostReport;
import com.lingshanel.studymoim.report.domain.ReportStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {

    boolean existsByStudyPostIdAndCommentIsNullAndReporterIdAndStatus(Long postId, Long reporterId, ReportStatus status);

    boolean existsByCommentIdAndReporterIdAndStatus(Long commentId, Long reporterId, ReportStatus status);

    void deleteByStudyPostId(Long postId);

    @Query("""
            select report
            from PostReport report
            join report.studyPost post
            join report.reporter reporter
            left join report.comment comment
            where (:status is null or report.status = :status)
              and (:keyword is null
                   or lower(post.title) like lower(concat('%', :keyword, '%'))
                   or lower(reporter.nickname) like lower(concat('%', :keyword, '%'))
                   or lower(report.reason) like lower(concat('%', :keyword, '%'))
                   or lower(comment.content) like lower(concat('%', :keyword, '%')))
            order by report.createdAt desc
            """)
    List<PostReport> searchForAdmin(@Param("keyword") String keyword, @Param("status") ReportStatus status);
}
