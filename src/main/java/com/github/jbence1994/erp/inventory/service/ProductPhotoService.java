package com.github.jbence1994.erp.inventory.service;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import com.github.jbence1994.erp.common.dto.PhotoDto;
import com.github.jbence1994.erp.common.service.PhotoService;
import com.github.jbence1994.erp.common.util.FileUtils;
import com.github.jbence1994.erp.common.validation.FileValidator;
import com.github.jbence1994.erp.inventory.dto.CreateProductPhotoDto;
import com.github.jbence1994.erp.inventory.dto.ProductPhotoDto;
import com.github.jbence1994.erp.inventory.exception.ProductAlreadyHasPhotoUploadedException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoNotFoundException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoUploadException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class ProductPhotoService implements PhotoService {
    private final ProductService productService;
    private final FileUtils fileUtils;
    private final FileValidator fileValidator;

    private static final String PRODUCTS_SUBDIRECTORY_NAME = "products";

    @Override
    public String uploadPhoto(CreatePhotoDto photo) {
        var productPhoto = (CreateProductPhotoDto) photo;

        try {
            fileValidator.validate(productPhoto);

            var product = productService.getProduct(productPhoto.getProductId());

            if (product.hasPhoto()) {
                throw new ProductAlreadyHasPhotoUploadedException(productPhoto.getProductId());
            }

            var photoName = photo.createFileName();
            fileUtils.store(
                    getPhotoUploadsPath(),
                    photoName,
                    productPhoto.getInputStream()
            );
            product.setPhotoFileName(photoName);
            productService.updateProduct(product);

            return product.getPhotoFileName();
        } catch (IOException exception) {
            throw new ProductPhotoUploadException(productPhoto.getProductId());
        }
    }

    @Override
    public PhotoDto getPhoto(Long id) {
        try {
            var product = productService.getProduct(id);

            byte[] photoBytes = fileUtils.read(
                    getPhotoUploadsPath(),
                    product.getPhotoFileName()
            );

            return new ProductPhotoDto(id, photoBytes, product.getPhotoFileExtension());
        } catch (Exception exception) {
            throw new ProductPhotoNotFoundException(id);
        }
    }

    // FIXME: Move to environment variable and YAML config
    private String getPhotoUploadsPath() {
        return String.format(
                "%s/%s/%s",
                UPLOADS_DIRECTORY_NAME,
                PHOTOS_SUBDIRECTORY_NAME,
                PRODUCTS_SUBDIRECTORY_NAME
        );
    }
}
