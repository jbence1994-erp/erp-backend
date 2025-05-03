package com.github.jbence1994.erp.inventory.testobject;

import com.github.jbence1994.erp.inventory.dto.CreateProductPhotoDto;

import static com.github.jbence1994.erp.inventory.constant.PhotoTestConstants.CONTENT_TYPE_IMAGE_JPEG;
import static com.github.jbence1994.erp.inventory.constant.PhotoTestConstants.FILE_NAME_JPEG;
import static com.github.jbence1994.erp.inventory.constant.PhotoTestConstants.FILE_SIZE;

public final class CreateProductPhotoDtoTestObject {
    public static CreateProductPhotoDto createProductPhotoDto() {
        return new CreateProductPhotoDto(
                1L,
                false,
                FILE_NAME_JPEG,
                FILE_SIZE.longValue(),
                CONTENT_TYPE_IMAGE_JPEG,
                new byte[FILE_SIZE]
        );
    }
}
