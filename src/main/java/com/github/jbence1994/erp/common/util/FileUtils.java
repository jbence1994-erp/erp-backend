package com.github.jbence1994.erp.common.util;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface FileUtils {
    String createPhotoUploadsDirectoryStructure(String customSubdirectoryName) throws IOException;

    String storePhoto(CreatePhotoDto photo, String directoryStructurePath) throws IOException;

    byte[] readAllBytes(String customSubdirectoryName, String fileName) throws IOException;
}
