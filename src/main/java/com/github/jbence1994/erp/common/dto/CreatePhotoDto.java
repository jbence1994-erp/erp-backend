package com.github.jbence1994.erp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@AllArgsConstructor
@Getter
public abstract class CreatePhotoDto {
    @Accessors(fluent = true)
    private boolean isEmpty;

    private String originalFilename;

    private Long size;

    private String contentType;

    private byte[] inputStreamBytes;

    public InputStream getInputStream() {
        return new ByteArrayInputStream(inputStreamBytes);
    }

    public String getFileExtension() {
        return StringUtils.getFilenameExtension(originalFilename);
    }
}
