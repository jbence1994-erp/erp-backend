package com.github.jbence1994.erp.common.util;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileSystemUtils implements FileUtils {
    private static final String UPLOADS_DIRECTORY_NAME = "uploads";
    private static final String PHOTOS_SUBDIRECTORY_NAME = "photos";

    @Override
    public String createPhotoUploadsDirectoryStructure(String customSubdirectoryName) throws IOException {
        var directoryPath = resolvePath(customSubdirectoryName);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        return directoryPath.toString();
    }

    @Override
    public String storePhoto(CreatePhotoDto photo, String directoryStructurePath) throws IOException {
        var fileName = String.format("%s.%s", UUID.randomUUID(), photo.getFileExtension());
        var pathWithFileName = Paths.get(directoryStructurePath, fileName);

        Files.copy(photo.getInputStream(), pathWithFileName);

        return fileName;
    }

    @Override
    public byte[] readAllBytes(String customSubdirectoryName, String fileName) throws IOException {
        var filePath = resolvePath(String.format("%s/%s", customSubdirectoryName, fileName));
        return Files.readAllBytes(filePath);
    }

    private Path resolvePath(String path) {
        return Paths.get(UPLOADS_DIRECTORY_NAME, PHOTOS_SUBDIRECTORY_NAME, path);
    }
}
