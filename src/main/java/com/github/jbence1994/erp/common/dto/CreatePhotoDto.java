package com.github.jbence1994.erp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

@AllArgsConstructor
@Getter
public abstract class CreatePhotoDto {
    private boolean isEmpty;

    private String originalFilename;

    private Long size;

    private String contentType;

    private byte[] inputStreamBytes;

    public abstract Long getEntityId();

    public InputStream getInputStream() {
        return new ByteArrayInputStream(inputStreamBytes);
    }

    public String createFileName() {
        return String.format("%s.%s", UUID.randomUUID(), getFileExtension());
    }

    public String getFileExtension() {
        return StringUtils.getFilenameExtension(originalFilename);
    }
}
