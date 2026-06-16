package com.lingshanel.studymoim.category;

import com.lingshanel.studymoim.category.domain.Category;
import com.lingshanel.studymoim.category.repository.CategoryRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class CategoryInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategoryInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        List<CategorySeed> seeds = List.of(
                new CategorySeed("백엔드", "backend"),
                new CategorySeed("프론트엔드", "frontend"),
                new CategorySeed("CS", "cs"),
                new CategorySeed("자격증", "certificate"),
                new CategorySeed("면접", "interview")
        );

        for (CategorySeed seed : seeds) {
            Category category = categoryRepository.findBySlug(seed.slug())
                    .orElseGet(() -> new Category(seed.name(), seed.slug()));
            category.updateName(seed.name());
            categoryRepository.save(category);
        }
    }

    private record CategorySeed(String name, String slug) {
    }
}
