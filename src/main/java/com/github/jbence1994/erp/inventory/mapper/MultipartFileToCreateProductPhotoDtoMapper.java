package com.github.jbence1994.erp.inventory.mapper;

import com.github.jbence1994.erp.inventory.dto.CreateProductPhotoDto;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoUploadException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class MultipartFileToCreateProductPhotoDtoMapper {
    public CreateProductPhotoDto toDto(Long productId, MultipartFile file) {
        try {
            return new CreateProductPhotoDto(
                    productId,
                    file.isEmpty(),
                    file.getOriginalFilename(),
                    file.getSize(),
                    file.getContentType(),
                    file.getBytes()
            );
        } catch (IOException exception) {
            throw new ProductPhotoUploadException(productId);
        }
    }
}
