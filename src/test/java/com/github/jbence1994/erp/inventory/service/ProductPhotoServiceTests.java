package com.github.jbence1994.erp.inventory.service;

import com.github.jbence1994.erp.common.util.FileUtils;
import com.github.jbence1994.erp.inventory.dto.CreateProductPhotoDto;
import com.github.jbence1994.erp.inventory.exception.ProductAlreadyHasPhotoUploadedException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoNotFoundException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoUploadException;
import com.github.jbence1994.erp.inventory.validation.FileValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.jbence1994.erp.inventory.constant.PhotoTestConstants.FILE_SIZE;
import static com.github.jbence1994.erp.inventory.constant.PhotoTestConstants.JPEG;
import static com.github.jbence1994.erp.inventory.constant.PhotoTestConstants.PHOTO_FILE_NAME;
import static com.github.jbence1994.erp.inventory.constant.PhotoTestConstants.UPLOADS_DIRECTORY_WITH_PHOTOS_SUBDIRECTORY_AND_CUSTOM_SUBDIRECTORY;
import static com.github.jbence1994.erp.inventory.testobject.ProductTestObject.product1;
import static com.github.jbence1994.erp.inventory.testobject.ProductTestObject.product1WithPhoto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductPhotoServiceTests {
    private final ProductService productService = mock(ProductService.class);
    private final FileUtils fileUtils = mock(FileUtils.class);
    private final FileValidator fileValidator = mock(FileValidator.class);
    private final ProductPhotoService productPhotoService = new ProductPhotoService(
            productService,
            fileUtils,
            fileValidator
    );
    private final CreateProductPhotoDto createProductPhotoDto = mock(CreateProductPhotoDto.class);

    @BeforeEach
    public void setup() {
        when(productService.getProduct(any())).thenReturn(product1WithPhoto());

        doNothing().when(fileValidator).validate(any());
    }

    @Test
    public void getPhotoTest_HappyPath() throws IOException {
        when(fileUtils.readAllBytes(any(), any())).thenReturn(new byte[FILE_SIZE]);

        var result = productPhotoService.getPhoto(1L);

        assertTrue(result.getPhotoBytes().length > 0);
        assertEquals(JPEG, result.getFileExtension());
    }

    @Test
    public void getPhotoTest_UnhappyPath() throws IOException {
        when(fileUtils.readAllBytes(any(), any())).thenThrow(new IOException());

        assertThrows(
                ProductPhotoNotFoundException.class,
                () -> productPhotoService.getPhoto(1L)
        );
    }

    @Test
    public void uploadPhotoTest_HappyPath() throws IOException {
        when(productService.getProduct(any())).thenReturn(product1());
        when(fileUtils.createPhotoUploadsDirectoryStructure(any()))
                .thenReturn(UPLOADS_DIRECTORY_WITH_PHOTOS_SUBDIRECTORY_AND_CUSTOM_SUBDIRECTORY);
        when(fileUtils.storePhoto(any(), any())).thenReturn(PHOTO_FILE_NAME);
        doNothing().when(productService).updateProduct(any());

        var result = productPhotoService.uploadPhoto(createProductPhotoDto);

        assertFalse(result.isBlank());
    }

    @Test
    public void uploadPhotoTest_UnhappyPath_ProductAlreadyHasPhotoUploaded() {
        assertThrows(
                ProductAlreadyHasPhotoUploadedException.class,
                () -> productPhotoService.uploadPhoto(createProductPhotoDto)
        );
    }

    @Test
    public void uploadPhotoTest_UnhappyPath_ProductPhotoUploadFailure() throws IOException {
        when(productService.getProduct(any())).thenReturn(product1());
        when(fileUtils.storePhoto(any(), any())).thenThrow(new IOException("Disk error"));

        assertThrows(
                ProductPhotoUploadException.class,
                () -> productPhotoService.uploadPhoto(createProductPhotoDto)
        );
    }
}
