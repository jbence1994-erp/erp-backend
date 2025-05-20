package com.github.jbence1994.erp.inventory.controller;

import com.github.jbence1994.erp.common.dto.PhotoResponse;
import com.github.jbence1994.erp.common.exception.EmptyFileException;
import com.github.jbence1994.erp.common.exception.InvalidFileExtensionException;
import com.github.jbence1994.erp.common.mapper.MultipartFileToCreatePhotoDtoMapper;
import com.github.jbence1994.erp.common.service.PhotoService;
import com.github.jbence1994.erp.inventory.dto.CreateProductPhotoDto;
import com.github.jbence1994.erp.inventory.dto.ProductPhotoDto;
import com.github.jbence1994.erp.inventory.exception.ProductAlreadyHasAPhotoUploadedException;
import com.github.jbence1994.erp.inventory.exception.ProductDoesNotHaveAPhotoUploadedYetException;
import com.github.jbence1994.erp.inventory.exception.ProductNotFoundException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoDeleteException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoDownloadException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoNotFoundException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoUploadException;
import com.github.jbence1994.erp.inventory.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Stream;

import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.IMAGE;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.JPEG;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.PHOTO_FILE_NAME;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.TXT;
import static com.github.jbence1994.erp.common.testobject.MultipartFileTestObject.emptyMultipartFile;
import static com.github.jbence1994.erp.common.testobject.MultipartFileTestObject.multipartFile;
import static com.github.jbence1994.erp.common.testobject.MultipartFileTestObject.multipartFileWithInvalidFileExtension;
import static com.github.jbence1994.erp.inventory.testobject.ProductPhotoDtoTestObject.productPhotoDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductPhotoControllerTests {

    @Mock
    private PhotoService<CreateProductPhotoDto, ProductPhotoDto, Product> photoService;

    @Mock
    private MultipartFileToCreatePhotoDtoMapper<CreateProductPhotoDto> toCreatePhotoDtoMapper;

    @InjectMocks
    private ProductPhotoController productPhotoController;

    @Test
    public void uploadProductPhotoTest_HappyPath() {
        when(photoService.uploadPhoto(any())).thenReturn(PHOTO_FILE_NAME);

        var result = productPhotoController.uploadProductPhoto(1L, multipartFile());

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(new PhotoResponse(PHOTO_FILE_NAME), result.getBody());
    }

    private static Stream<Arguments> uploadProductPhotoUnhappyPathParams() {
        return Stream.of(
                Arguments.of(
                        "ProductNotFoundException - HTTP 404",
                        new ProductNotFoundException(1L),
                        1L,
                        multipartFile(),
                        HttpStatus.NOT_FOUND,
                        "Termék a következő azonosítóval: #1 nem található"
                ),
                Arguments.of(
                        "EmptyFileException - HTTP 400",
                        new EmptyFileException(emptyMultipartFile().getOriginalFilename()),
                        1L,
                        emptyMultipartFile(),
                        HttpStatus.BAD_REQUEST,
                        "Üres fájl: file_name.jpeg"
                ),
                Arguments.of(
                        "InvalidFileNameException - HTTP 400",
                        new InvalidFileExtensionException(TXT),
                        1L,
                        multipartFileWithInvalidFileExtension(),
                        HttpStatus.BAD_REQUEST,
                        "Hibás fájlformátum: TXT"
                ),
                Arguments.of(
                        "ProductAlreadyHasAPhotoUploadedException - HTTP 400",
                        new ProductAlreadyHasAPhotoUploadedException(1L),
                        1L,
                        multipartFile(),
                        HttpStatus.BAD_REQUEST,
                        "Termék a következő azonosítóval: #1 már rendelkezik feltöltött fényképpel"
                ),
                Arguments.of(
                        "ProductPhotoUploadException - HTTP 500",
                        new ProductPhotoUploadException(1L),
                        1L,
                        multipartFile(),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Fénykép feltöltése az alábbi termékhez: #1 sikeretelen volt"
                )
        );
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("uploadProductPhotoUnhappyPathParams")
    public void uploadProductPhotoTest_UnhappyPaths(
            String testCase,
            Exception exception,
            Long productId,
            MultipartFile multipartFile,
            HttpStatus httpStatus,
            String exceptionMessage
    ) {
        when(photoService.uploadPhoto(any())).thenThrow(exception);

        var result = productPhotoController.uploadProductPhoto(productId, multipartFile);

        assertEquals(httpStatus, result.getStatusCode());
        assertEquals(exceptionMessage, result.getBody());
    }

    @Test
    public void getProductPhotoTest_HappyPath() {
        when(photoService.getPhoto(any())).thenReturn(productPhotoDto());

        var result = productPhotoController.getProductPhoto(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(IMAGE, result.getHeaders().getContentType().getType());
        assertEquals(JPEG, result.getHeaders().getContentType().getSubtype());
        assertInstanceOf(byte[].class, result.getBody());
    }

    static Stream<Arguments> getProductPhotoUnhappyPathParams() {
        return Stream.of(
                Arguments.of(
                        "ProductNotFoundException - HTTP 400",
                        new ProductNotFoundException(1L),
                        1L,
                        HttpStatus.NOT_FOUND,
                        "Termék a következő azonosítóval: #1 nem található"
                ),
                Arguments.of(
                        "ProductPhotoNotFoundException - HTTP 400",
                        new ProductPhotoNotFoundException(1L),
                        1L,
                        HttpStatus.NOT_FOUND,
                        "Termék a következő azonosítóval: #1 nem rendelkezik feltöltött fényképpel"
                ),
                Arguments.of(
                        "ProductPhotoDownloadException - HTTP 500",
                        new ProductPhotoDownloadException(1L),
                        1L,
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Fénykép letöltése az alábbi termékhez: #1 sikeretelen volt"
                )
        );
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("getProductPhotoUnhappyPathParams")
    public void getProductPhotoTest_UnhappyPaths(
            String testCase,
            Exception exception,
            Long productId,
            HttpStatus httpStatus,
            String exceptionMessage
    ) {
        when(photoService.getPhoto(any())).thenThrow(exception);

        var result = productPhotoController.getProductPhoto(productId);

        assertEquals(httpStatus, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(exceptionMessage, result.getBody().toString());
    }

    @Test
    public void deleteProductPhotoTest_HappyPath() {
        doNothing().when(photoService).deletePhoto(any());

        var result = productPhotoController.deleteProductPhoto(1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    static Stream<Arguments> deleteProductPhotoUnhappyPathParams() {
        return Stream.of(
                Arguments.of(
                        "ProductDoesNotHaveAPhotoUploadedYetException - HTTP 400",
                        new ProductDoesNotHaveAPhotoUploadedYetException(1L),
                        1L,
                        HttpStatus.BAD_REQUEST,
                        "Termék a következő azonosítóval: #1 még nem rendelkezik feltöltött fényképpel"
                ),
                Arguments.of(
                        "ProductPhotoDeleteException - HTTP 500",
                        new ProductPhotoDeleteException(1L),
                        1L,
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Fénykép törlése az alábbi terméknél: #1 sikeretelen volt"
                )
        );
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("deleteProductPhotoUnhappyPathParams")
    public void deleteProductPhotoTest_UnhappyPaths(
            String testCase,
            Exception exception,
            Long productId,
            HttpStatus httpStatus,
            String exceptionMessage
    ) {
        doThrow(exception).when(photoService).deletePhoto(any());

        var result = productPhotoController.deleteProductPhoto(productId);

        assertEquals(httpStatus, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(exceptionMessage, result.getBody().toString());
    }
}
