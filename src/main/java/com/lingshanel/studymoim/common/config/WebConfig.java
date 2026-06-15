package com.lingshanel.studymoim.common.config;

import com.lingshanel.studymoim.common.file.UploadPathProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UploadPathProvider uploadPathProvider;

    public WebConfig(UploadPathProvider uploadPathProvider) {
        this.uploadPathProvider = uploadPathProvider;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPathProvider.resourceLocation());
    }
}
