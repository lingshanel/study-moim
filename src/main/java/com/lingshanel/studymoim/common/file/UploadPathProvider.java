package com.lingshanel.studymoim.common.file;

import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UploadPathProvider {

    private final Path root;

    public UploadPathProvider(@Value("${study-moim.upload.directory:uploads}") String directory) {
        this.root = Path.of(directory).toAbsolutePath().normalize();
    }

    public Path root() {
        return root;
    }

    public String resourceLocation() {
        String location = root.toUri().toString();
        return location.endsWith("/") ? location : location + "/";
    }
}
