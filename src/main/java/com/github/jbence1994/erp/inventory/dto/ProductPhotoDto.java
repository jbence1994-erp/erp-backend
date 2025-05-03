package com.github.jbence1994.erp.inventory.dto;

import com.github.jbence1994.erp.common.dto.PhotoDto;
import lombok.Getter;

@Getter
public class ProductPhotoDto extends PhotoDto {
    private final Long productId;

    public ProductPhotoDto(Long productId, byte[] photoBytes, String fileExtension) {
        super(photoBytes, fileExtension);
        this.productId = productId;
    }
}
