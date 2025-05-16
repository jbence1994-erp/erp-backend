package com.github.jbence1994.erp.inventory.dto;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;

public class CreateProductPhotoDto extends CreatePhotoDto {
    private final Long productId;

    public CreateProductPhotoDto(
            Long productId,
            boolean isEmpty,
            String originalFilename,
            Long size,
            String contentType,
            byte[] inputStreamBytes
    ) {
        super(isEmpty, originalFilename, size, contentType, inputStreamBytes);
        this.productId = productId;
    }

    @Override
    public Long getEntityId() {
        return productId;
    }
}
