package com.github.jbence1994.erp.inventory.service;

import com.github.jbence1994.erp.common.service.PhotoService;
import com.github.jbence1994.erp.common.util.FileUtils;
import com.github.jbence1994.erp.common.validation.FileValidator;
import com.github.jbence1994.erp.inventory.dto.CreateProductPhotoDto;
import com.github.jbence1994.erp.inventory.dto.ProductPhotoDto;
import com.github.jbence1994.erp.inventory.exception.ProductAlreadyHasAPhotoUploadedException;
import com.github.jbence1994.erp.inventory.exception.ProductDoesNotHaveAPhotoUploadedYetException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoDeleteException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoDownloadException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoNotFoundException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoUploadException;
import com.github.jbence1994.erp.inventory.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProductPhotoService extends PhotoService<CreateProductPhotoDto, ProductPhotoDto, Product> {
    private final ProductService productService;

    public ProductPhotoService(
            @Value("${erp.photo-upload-directory-path.products}") final String photoUploadDirectoryPath,
            final ProductService productService,
            final FileUtils fileUtils,
            final FileValidator fileValidator
    ) {
        super(fileUtils, fileValidator);
        this.productService = productService;
        this.photoUploadDirectoryPath = photoUploadDirectoryPath;
    }

    @Override
    protected Product getEntity(Long id) {
        return productService.getProduct(id);
    }

    @Override
    protected void updateEntity(Product product) {
        productService.updateProduct(product);
    }

    @Override
    protected String createFileName(CreateProductPhotoDto createProductPhotoDto) {
        return createProductPhotoDto.createFileName();
    }

    @Override
    protected ProductPhotoDto dto(Long id, byte[] photoBytes, String extension) {
        return new ProductPhotoDto(id, photoBytes, extension);
    }

    @Override
    protected RuntimeException alreadyHasAPhotoUploadedException(Long id) {
        return new ProductAlreadyHasAPhotoUploadedException(id);
    }

    @Override
    protected RuntimeException doesNotHaveAPhotoUploadedYetException(Long id) {
        return new ProductDoesNotHaveAPhotoUploadedYetException(id);
    }

    @Override
    protected RuntimeException photoUploadException(Long id) {
        return new ProductPhotoUploadException(id);
    }

    @Override
    protected RuntimeException photoDownloadException(Long id) {
        return new ProductPhotoDownloadException(id);
    }

    @Override
    protected RuntimeException photoDeleteException(Long id) {
        return new ProductPhotoDeleteException(id);
    }

    @Override
    protected RuntimeException photoNotFoundException(Long id) {
        return new ProductPhotoNotFoundException(id);
    }
}
