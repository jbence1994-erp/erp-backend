package com.github.jbence1994.erp.identity.testobject;

import com.github.jbence1994.erp.identity.dto.UserProfilePhotoDto;

import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.FILE_SIZE;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.JPEG;

public final class UserProfilePhotoDtoTestObject {
    public static UserProfilePhotoDto userProfilePhotoDto() {
        return new UserProfilePhotoDto(
                1L,
                new byte[FILE_SIZE],
                JPEG
        );
    }
}
