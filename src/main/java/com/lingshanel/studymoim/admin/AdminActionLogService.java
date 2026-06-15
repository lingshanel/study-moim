package com.lingshanel.studymoim.admin;

import com.lingshanel.studymoim.admin.domain.AdminActionLog;
import com.lingshanel.studymoim.admin.dto.AdminActionLogResponse;
import com.lingshanel.studymoim.admin.repository.AdminActionLogRepository;
import com.lingshanel.studymoim.common.error.ForbiddenException;
import com.lingshanel.studymoim.user.domain.User;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminActionLogService {

    private final AdminActionLogRepository repository;

    public AdminActionLogService(AdminActionLogRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void record(String actionType, String targetType, Long targetId, String summary, User admin) {
        repository.save(new AdminActionLog(
                actionType,
                targetType,
                targetId,
                summary,
                new AdminActionLog.UserSnapshot(admin.getId(), admin.getNickname())
        ));
    }

    @Transactional(readOnly = true)
    public List<AdminActionLogResponse> getRecentLogs(User admin) {
        if (!admin.isAdmin()) {
            throw new ForbiddenException("관리자 권한이 필요합니다.");
        }
        return repository.findTop100ByOrderByCreatedAtDesc().stream()
                .map(AdminActionLogResponse::from)
                .toList();
    }
}
