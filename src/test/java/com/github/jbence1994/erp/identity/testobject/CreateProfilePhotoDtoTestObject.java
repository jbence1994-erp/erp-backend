package com.github.jbence1994.erp.identity.testobject;

import com.github.jbence1994.erp.identity.dto.CreateProfilePhotoDto;

import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.CONTENT_TYPE_IMAGE_JPEG;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.FILE_NAME_JPEG;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.FILE_SIZE;

public final class CreateProfilePhotoDtoTestObject {
    public static CreateProfilePhotoDto createProfilePhotoDto() {
        return new CreateProfilePhotoDto(
                1L,
                false,
                FILE_NAME_JPEG,
                FILE_SIZE.longValue(),
                CONTENT_TYPE_IMAGE_JPEG,
                new byte[FILE_SIZE]
        );
    }
}
