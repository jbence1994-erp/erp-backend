package com.github.jbence1994.erp.inventory.testobject;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static com.github.jbence1994.erp.inventory.constant.PhotoTestConstants.CONTENT_TYPE_IMAGE_JPEG;
import static com.github.jbence1994.erp.inventory.constant.PhotoTestConstants.FILE_NAME_JPEG;
import static com.github.jbence1994.erp.inventory.constant.PhotoTestConstants.FILE_SIZE;
import static com.github.jbence1994.erp.inventory.constant.PhotoTestConstants.INVALID_FILE_NAME;
import static com.github.jbence1994.erp.inventory.constant.PhotoTestConstants.TEXT_PLAIN;

public final class MultipartFileTestObject {
    public static MultipartFile multipartFile() {
        return new MockMultipartFile(
                FILE_NAME_JPEG,
                FILE_NAME_JPEG,
                CONTENT_TYPE_IMAGE_JPEG,
                new byte[FILE_SIZE]
        );
    }

    public static MultipartFile emptyMultipartFile() {
        return new MockMultipartFile(
                FILE_NAME_JPEG,
                FILE_NAME_JPEG,
                CONTENT_TYPE_IMAGE_JPEG,
                new byte[0]
        );
    }

    public static MultipartFile multipartFileWithInvalidFileExtension() {
        return new MockMultipartFile(
                INVALID_FILE_NAME,
                INVALID_FILE_NAME,
                TEXT_PLAIN,
                new byte[FILE_SIZE]
        );
    }
}
