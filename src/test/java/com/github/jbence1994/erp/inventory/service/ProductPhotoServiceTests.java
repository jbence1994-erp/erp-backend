package com.github.jbence1994.erp.inventory.service;

import com.github.jbence1994.erp.common.util.FileUtils;
import com.github.jbence1994.erp.common.validation.FileValidator;
import com.github.jbence1994.erp.inventory.dto.CreateProductPhotoDto;
import com.github.jbence1994.erp.inventory.exception.ProductAlreadyHasPhotoUploadedException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoNotFoundException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoUploadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.FILE_SIZE;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.JPEG;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.PHOTO_FILE_NAME;
import static com.github.jbence1994.erp.inventory.testobject.ProductTestObject.product1;
import static com.github.jbence1994.erp.inventory.testobject.ProductTestObject.product1WithPhoto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductPhotoServiceTests {

    @Mock
    private ProductService productService;

    @Mock
    private FileUtils fileUtils;

    @Mock
    private FileValidator fileValidator;

    @InjectMocks
    private ProductPhotoService productPhotoService;

    @Mock
    private CreateProductPhotoDto createProductPhotoDto;

    @BeforeEach
    public void setup() {
        when(productService.getProduct(any())).thenReturn(product1WithPhoto());
    }

    @Test
    public void getPhotoTest_HappyPath() throws IOException {
        when(fileUtils.read(any(), any())).thenReturn(new byte[FILE_SIZE]);

        var result = productPhotoService.getPhoto(1L);

        assertTrue(result.getPhotoBytes().length > 0);
        assertEquals(JPEG, result.getFileExtension());
    }

    @Test
    public void getPhotoTest_UnhappyPath() throws IOException {
        when(fileUtils.read(any(), any())).thenThrow(new IOException());

        assertThrows(
                ProductPhotoNotFoundException.class,
                () -> productPhotoService.getPhoto(1L)
        );
    }

    @Test
    public void uploadPhotoTest_HappyPath() throws IOException {
        when(productService.getProduct(any())).thenReturn(product1());
        when(createProductPhotoDto.createFileName()).thenReturn(PHOTO_FILE_NAME);
        doNothing().when(fileUtils).store(any(), any(), any());
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
        doThrow(new IOException("Disk error")).when(fileUtils).store(any(), any(), any());

        assertThrows(
                ProductPhotoUploadException.class,
                () -> productPhotoService.uploadPhoto(createProductPhotoDto)
        );
    }
}
