package com.github.jbence1994.erp.inventory.testobject;

import com.github.jbence1994.erp.inventory.dto.ProductPhotoDto;

import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.FILE_SIZE;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.JPEG;

public final class ProductPhotoDtoTestObject {
    public static ProductPhotoDto productPhotoDto() {
        return new ProductPhotoDto(
                1L,
                new byte[FILE_SIZE],
                JPEG
        );
    }
}
