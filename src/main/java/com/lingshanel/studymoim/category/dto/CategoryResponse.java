package com.lingshanel.studymoim.category.dto;

import com.lingshanel.studymoim.category.domain.Category;

public record CategoryResponse(
        Long id,
        String name,
        String slug
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getSlug());
    }
}
