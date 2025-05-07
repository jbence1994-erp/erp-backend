package com.github.jbence1994.erp.inventory.util;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import com.github.jbence1994.erp.common.util.FileUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static com.github.jbence1994.erp.common.constant.FileConstants.PHOTOS_SUBDIRECTORY_NAME;
import static com.github.jbence1994.erp.common.constant.FileConstants.UPLOADS_DIRECTORY_NAME;

@Component
public class FileSystemUtils implements FileUtils {
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
