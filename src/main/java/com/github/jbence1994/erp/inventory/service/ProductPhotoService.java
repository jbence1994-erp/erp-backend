package com.github.jbence1994.erp.inventory.service;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import com.github.jbence1994.erp.common.dto.PhotoDto;
import com.github.jbence1994.erp.common.service.PhotoService;
import com.github.jbence1994.erp.common.util.FileUtils;
import com.github.jbence1994.erp.inventory.dto.CreateProductPhotoDto;
import com.github.jbence1994.erp.inventory.dto.ProductPhotoDto;
import com.github.jbence1994.erp.inventory.exception.ProductAlreadyHasPhotoUploadedException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoNotFoundException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoUploadException;
import com.github.jbence1994.erp.inventory.validation.FileValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.github.jbence1994.erp.inventory.config.FileConstants.PRODUCTS_SUBDIRECTORY_NAME;

@Service
@AllArgsConstructor
public class ProductPhotoService implements PhotoService {
    private final ProductService productService;
    private final FileUtils fileUtils;
    private final FileValidator fileValidator;

    @Override
    public PhotoDto getPhoto(Long id) {
        var product = productService.getProduct(id);

        byte[] productPhotoBytes;

        try {
            productPhotoBytes = fileUtils.readAllBytes(PRODUCTS_SUBDIRECTORY_NAME, product.getPhotoFileName());
        } catch (Exception exception) {
            throw new ProductPhotoNotFoundException(id);
        }

        return new ProductPhotoDto(id, productPhotoBytes, product.getPhotoFileExtension());
    }

    @Override
    public String uploadPhoto(CreatePhotoDto photo) {
        var productPhoto = (CreateProductPhotoDto) photo;

        try {
            fileValidator.validate(productPhoto);

            var product = productService.getProduct(productPhoto.getProductId());

            if (product.hasPhoto()) {
                throw new ProductAlreadyHasPhotoUploadedException(productPhoto.getProductId());
            }

            var directoryStructurePath = fileUtils.createPhotoUploadsDirectoryStructure(PRODUCTS_SUBDIRECTORY_NAME);
            product.setPhotoFileName(fileUtils.storePhoto(productPhoto, directoryStructurePath));
            productService.updateProduct(product);

            return product.getPhotoFileName();
        } catch (IOException exception) {
            throw new ProductPhotoUploadException(productPhoto.getProductId());
        }
    }
}
