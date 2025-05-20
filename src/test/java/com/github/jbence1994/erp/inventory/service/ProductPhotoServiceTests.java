package com.github.jbence1994.erp.inventory.service;

import com.github.jbence1994.erp.common.util.FileUtils;
import com.github.jbence1994.erp.common.validation.FileValidator;
import com.github.jbence1994.erp.inventory.dto.CreateProductPhotoDto;
import com.github.jbence1994.erp.inventory.exception.ProductAlreadyHasAPhotoUploadedException;
import com.github.jbence1994.erp.inventory.exception.ProductDoesNotHaveAPhotoUploadedYetException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoDeleteException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoDownloadException;
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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
    public void uploadPhotoTest_HappyPath() throws IOException {
        when(productService.getProduct(any())).thenReturn(product1());
        when(createProductPhotoDto.createFileName()).thenReturn(PHOTO_FILE_NAME);
        doNothing().when(fileUtils).store(any(), any(), any());
        doNothing().when(productService).updateProduct(any());

        var result = productPhotoService.uploadPhoto(createProductPhotoDto);

        assertFalse(result.isBlank());
    }

    @Test
    public void uploadPhotoTest_UnhappyPath_ProductAlreadyHasAPhotoUploaded() {
        assertThrows(
                ProductAlreadyHasAPhotoUploadedException.class,
                () -> productPhotoService.uploadPhoto(createProductPhotoDto)
        );
    }

    @Test
    public void uploadPhotoTest_UnhappyPath_IOException() throws IOException {
        when(productService.getProduct(any())).thenReturn(product1());
        doThrow(new IOException("Disk error")).when(fileUtils).store(any(), any(), any());

        assertThrows(
                ProductPhotoUploadException.class,
                () -> productPhotoService.uploadPhoto(createProductPhotoDto)
        );
    }

    @Test
    public void getPhotoTest_HappyPath() throws IOException {
        when(fileUtils.read(any(), any())).thenReturn(new byte[FILE_SIZE]);

        var result = productPhotoService.getPhoto(1L);

        assertTrue(result.getPhotoBytes().length > 0);
        assertEquals(JPEG, result.getFileExtension());
    }

    @Test
    public void getPhotoTest_UnhappyPath_ProductHasNoPhoto() {
        when(productService.getProduct(any())).thenReturn(product1());

        assertThrows(
                ProductPhotoNotFoundException.class,
                () -> productPhotoService.getPhoto(1L)
        );
    }

    @Test
    public void getPhotoTest_UnhappyPath_IOException() throws IOException {
        when(fileUtils.read(any(), any())).thenThrow(new IOException("Disk error"));

        assertThrows(
                ProductPhotoDownloadException.class,
                () -> productPhotoService.getPhoto(1L)
        );
    }

    @Test
    public void deletePhotoTest_HappyPath() throws IOException {
        doNothing().when(fileUtils).delete(any(), any());

        assertDoesNotThrow(() -> productPhotoService.deletePhoto(1L));
    }

    @Test
    public void deletePhotoTest_UnhappyPath_ProductDoesNotHaveAPhotoUploadedYetException() {
        when(productService.getProduct(any())).thenReturn(product1());

        assertThrows(
                ProductDoesNotHaveAPhotoUploadedYetException.class,
                () -> productPhotoService.deletePhoto(1L)
        );
    }

    @Test
    public void deletePhotoTest_UnhappyPath_IOException() throws IOException {
        doThrow(new IOException("Disk error")).when(fileUtils).delete(any(), any());

        assertThrows(
                ProductPhotoDeleteException.class,
                () -> productPhotoService.deletePhoto(1L)
        );
    }
}
