package com.lingshanel.studymoim.admin.repository;

import com.lingshanel.studymoim.admin.domain.AdminActionLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminActionLogRepository extends JpaRepository<AdminActionLog, Long> {
    List<AdminActionLog> findTop100ByOrderByCreatedAtDesc();
}
