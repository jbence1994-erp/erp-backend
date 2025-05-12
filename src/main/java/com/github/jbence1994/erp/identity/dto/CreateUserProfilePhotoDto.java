package com.github.jbence1994.erp.identity.dto;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import lombok.Getter;

@Getter
public class CreateUserProfilePhotoDto extends CreatePhotoDto {
    private final Long userProfileId;

    public CreateUserProfilePhotoDto(
            Long userProfileId,
            boolean isEmpty,
            String originalFilename,
            Long size,
            String contentType,
            byte[] inputStreamBytes
    ) {
        super(isEmpty, originalFilename, size, contentType, inputStreamBytes);
        this.userProfileId = userProfileId;
    }
}
