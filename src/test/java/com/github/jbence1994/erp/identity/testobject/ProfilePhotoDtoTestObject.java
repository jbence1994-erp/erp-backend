package com.github.jbence1994.erp.identity.testobject;

import com.github.jbence1994.erp.identity.dto.ProfilePhotoDto;

import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.FILE_SIZE;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.JPEG;

public final class ProfilePhotoDtoTestObject {
    public static ProfilePhotoDto profilePhotoDto() {
        return new ProfilePhotoDto(
                1L,
                new byte[FILE_SIZE],
                JPEG
        );
    }
}
