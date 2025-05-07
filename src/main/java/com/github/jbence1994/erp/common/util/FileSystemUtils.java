package com.github.jbence1994.erp.common.util;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileSystemUtils implements FileUtils {
    private static final String UPLOADS_DIRECTORY_NAME = "uploads";
    private static final String PHOTOS_SUBDIRECTORY_NAME = "photos";

    @Override
    public byte[] readAllBytes(String customSubdirectoryName, String fileName) throws IOException {
        var uploadsDirectoryWithPhotosSubdirectoryAndCustomSubdirectory = Paths.get(
                UPLOADS_DIRECTORY_NAME,
                PHOTOS_SUBDIRECTORY_NAME,
                customSubdirectoryName,
                fileName
        );

        return Files.readAllBytes(uploadsDirectoryWithPhotosSubdirectoryAndCustomSubdirectory);
    }

    @Override
    public String createPhotoUploadsDirectoryStructure(String customSubdirectoryName) throws IOException {
        var uploadsDirectoryWithPhotosSubdirectoryAndCustomSubdirectory = Paths.get(
                UPLOADS_DIRECTORY_NAME,
                PHOTOS_SUBDIRECTORY_NAME,
                customSubdirectoryName
        );

        if (!Files.exists(uploadsDirectoryWithPhotosSubdirectoryAndCustomSubdirectory)) {
            Files.createDirectories(uploadsDirectoryWithPhotosSubdirectoryAndCustomSubdirectory);
        }

        return uploadsDirectoryWithPhotosSubdirectoryAndCustomSubdirectory.toString();
    }

    @Override
    public String storePhoto(CreatePhotoDto photo, String directoryStructurePath) throws IOException {
        var fileName = String.format("%s.%s", UUID.randomUUID(), photo.getFileExtension());
        var pathWithFileName = Paths.get(directoryStructurePath, fileName);

        Files.copy(photo.getInputStream(), pathWithFileName);

        return fileName;
    }
}
