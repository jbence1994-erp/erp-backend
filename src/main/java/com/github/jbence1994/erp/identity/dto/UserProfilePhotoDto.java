package com.github.jbence1994.erp.identity.dto;

import com.github.jbence1994.erp.common.dto.PhotoDto;
import lombok.Getter;

@Getter
public class UserProfilePhotoDto extends PhotoDto {
    private final Long userProfileId;

    public UserProfilePhotoDto(Long userProfileId, byte[] photoBytes, String fileExtension) {
        super(photoBytes, fileExtension);
        this.userProfileId = userProfileId;
    }
}
