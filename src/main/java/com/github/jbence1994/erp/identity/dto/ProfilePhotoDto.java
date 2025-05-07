package com.github.jbence1994.erp.identity.dto;

import com.github.jbence1994.erp.common.dto.PhotoDto;
import lombok.Getter;

@Getter
public class ProfilePhotoDto extends PhotoDto {
    private final Long profileId;

    public ProfilePhotoDto(Long profileId, byte[] photoBytes, String fileExtension) {
        super(photoBytes, fileExtension);
        this.profileId = profileId;
    }
}
