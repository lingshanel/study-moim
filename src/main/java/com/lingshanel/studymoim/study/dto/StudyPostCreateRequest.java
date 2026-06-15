package com.lingshanel.studymoim.study.dto;

import com.lingshanel.studymoim.study.domain.StudyType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StudyPostCreateRequest(
        @NotBlank(message = "카테고리를 선택해주세요.")
        String categorySlug,

        @NotBlank(message = "제목을 입력해주세요.")
        @Size(max = 120, message = "제목은 120자 이하로 입력해주세요.")
        String title,

        @NotBlank(message = "내용을 입력해주세요.")
        String content,

        @NotNull(message = "스터디 진행 방식을 선택해주세요.")
        StudyType studyType,

        @Size(max = 120, message = "장소는 120자 이하로 입력해주세요.")
        String location,

        @Min(value = 1, message = "모집 인원은 1명 이상으로 입력해주세요.")
        @Max(value = 100, message = "모집 인원은 100명 이하로 입력해주세요.")
        Integer maxMembers
) {
}
