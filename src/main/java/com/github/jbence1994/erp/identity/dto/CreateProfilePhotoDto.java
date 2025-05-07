package com.github.jbence1994.erp.identity.dto;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import lombok.Getter;

@Getter
public class CreateProfilePhotoDto extends CreatePhotoDto {
    private final Long profileId;

    public CreateProfilePhotoDto(
            Long profileId,
            boolean isEmpty,
            String originalFilename,
            Long size,
            String contentType,
            byte[] inputStreamBytes
    ) {
        super(isEmpty, originalFilename, size, contentType, inputStreamBytes);
        this.profileId = profileId;
    }
}
