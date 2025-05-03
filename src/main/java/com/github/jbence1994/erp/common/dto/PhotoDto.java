package com.github.jbence1994.erp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class PhotoDto {
    private final byte[] photoBytes;
    private final String fileExtension;
}
