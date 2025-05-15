package com.github.jbence1994.erp.inventory.service;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import com.github.jbence1994.erp.common.dto.PhotoDto;
import com.github.jbence1994.erp.common.service.PhotoService;
import com.github.jbence1994.erp.common.util.FileUtils;
import com.github.jbence1994.erp.common.validation.FileValidator;
import com.github.jbence1994.erp.inventory.dto.CreateProductPhotoDto;
import com.github.jbence1994.erp.inventory.dto.ProductPhotoDto;
import com.github.jbence1994.erp.inventory.exception.ProductAlreadyHasPhotoUploadedException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoDownloadException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoNotFoundException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProductPhotoService implements PhotoService {
    private final ProductService productService;
    private final FileUtils fileUtils;
    private final FileValidator fileValidator;

    @Value("${erp.photo-upload-directory-path.products}")
    private String photoUploadDirectoryPath;

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
                    photoUploadDirectoryPath,
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

            if (!product.hasPhoto()) {
                throw new ProductPhotoNotFoundException(id);
            }

            var photoBytes = fileUtils.read(
                    photoUploadDirectoryPath,
                    product.getPhotoFileName()
            );

            return new ProductPhotoDto(id, photoBytes, product.getPhotoFileExtension());
        } catch (IOException exception) {
            throw new ProductPhotoDownloadException(id);
        }
    }
}
