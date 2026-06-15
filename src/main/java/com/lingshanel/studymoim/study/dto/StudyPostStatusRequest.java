package com.lingshanel.studymoim.study.dto;

import com.lingshanel.studymoim.study.domain.RecruitmentStatus;
import jakarta.validation.constraints.NotNull;

public record StudyPostStatusRequest(
        @NotNull(message = "모집 상태를 선택해주세요.")
        RecruitmentStatus recruitmentStatus
) {
}
