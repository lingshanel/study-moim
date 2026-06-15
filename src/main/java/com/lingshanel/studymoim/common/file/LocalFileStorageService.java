package com.lingshanel.studymoim.common.file;

import com.lingshanel.studymoim.common.error.BadRequestException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalFileStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp"
    );
    private static final long MAX_SIZE = 5 * 1024 * 1024;

    private final Path uploadRoot;

    public LocalFileStorageService(UploadPathProvider uploadPathProvider) {
        this.uploadRoot = uploadPathProvider.root();
    }

    public String storeStudyPostImage(MultipartFile file) {
        validate(file);

        try {
            Files.createDirectories(uploadRoot);
            String extension = extensionOf(file.getOriginalFilename());
            String storedName = UUID.randomUUID() + extension;
            Path target = uploadRoot.resolve(storedName).normalize();
            file.transferTo(target);
            return "/uploads/" + storedName;
        } catch (IOException exception) {
            throw new BadRequestException("이미지를 저장하지 못했습니다.");
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("업로드할 이미지를 선택해주세요.");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new BadRequestException("이미지는 5MB 이하만 업로드할 수 있습니다.");
        }
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new BadRequestException("jpg, png, gif, webp 이미지만 업로드할 수 있습니다.");
        }
    }

    private String extensionOf(String originalFilename) {
        String filename = StringUtils.cleanPath(originalFilename == null ? "" : originalFilename);
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0) {
            return "";
        }
        return filename.substring(dotIndex).toLowerCase();
    }
}
