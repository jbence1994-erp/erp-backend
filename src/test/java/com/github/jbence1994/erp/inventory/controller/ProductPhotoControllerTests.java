package com.github.jbence1994.erp.inventory.controller;

import com.github.jbence1994.erp.inventory.exception.EmptyFileException;
import com.github.jbence1994.erp.inventory.exception.InvalidFileExtensionException;
import com.github.jbence1994.erp.inventory.exception.ProductAlreadyHasPhotoUploadedException;
import com.github.jbence1994.erp.inventory.exception.ProductNotFoundException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoNotFoundException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoUploadException;
import com.github.jbence1994.erp.inventory.mapper.MultipartFileToCreateProductPhotoDtoMapper;
import com.github.jbence1994.erp.inventory.service.ProductPhotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.stream.Stream;

import static com.github.jbence1994.erp.inventory.constant.PhotoTestConstants.IMAGE;
import static com.github.jbence1994.erp.inventory.constant.PhotoTestConstants.JPEG;
import static com.github.jbence1994.erp.inventory.constant.PhotoTestConstants.PHOTO_FILE_NAME;
import static com.github.jbence1994.erp.inventory.constant.PhotoTestConstants.PHOTO_FILE_NAME_KEY;
import static com.github.jbence1994.erp.inventory.constant.PhotoTestConstants.TXT;
import static com.github.jbence1994.erp.inventory.testobject.CreateProductPhotoDtoTestObject.createProductPhotoDto;
import static com.github.jbence1994.erp.inventory.testobject.MultipartFileTestObject.emptyMultipartFile;
import static com.github.jbence1994.erp.inventory.testobject.MultipartFileTestObject.multipartFile;
import static com.github.jbence1994.erp.inventory.testobject.MultipartFileTestObject.multipartFileWithInvalidFileExtension;
import static com.github.jbence1994.erp.inventory.testobject.ProductPhotoDtoTestObject.productPhotoDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductPhotoControllerTests {
    private final ProductPhotoService productPhotoService = mock(ProductPhotoService.class);
    private final MultipartFileToCreateProductPhotoDtoMapper toCreateProductPhotoDtoMapper = mock(MultipartFileToCreateProductPhotoDtoMapper.class);
    private final ProductPhotoController productPhotoController = new ProductPhotoController(productPhotoService, toCreateProductPhotoDtoMapper);

    @BeforeEach
    public void setup() {
        when(toCreateProductPhotoDtoMapper.toDto(any(), any())).thenReturn(createProductPhotoDto());
    }

    @Test
    public void getProductPhotoTest_HappyPath() {
        when(productPhotoService.getPhoto(any())).thenReturn(productPhotoDto());

        var result = productPhotoController.getProductPhoto(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(IMAGE, result.getHeaders().getContentType().getType());
        assertEquals(JPEG, result.getHeaders().getContentType().getSubtype());
        assertInstanceOf(byte[].class, result.getBody());
    }

    @Test
    public void getProductPhotoTest_UnhappyPath_ProductPhotoNotFound() {
        when(productPhotoService.getPhoto(any())).thenThrow(new ProductPhotoNotFoundException(3L));

        var result = productPhotoController.getProductPhoto(3L);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Termék a következő azonosítóval: #3 nem rendelkezik feltöltött fényképpel", result.getBody().toString());
    }

    @Test
    public void uploadProductPhotoTest_HappyPath() {
        when(productPhotoService.uploadPhoto(any())).thenReturn(PHOTO_FILE_NAME);

        var result = productPhotoController.uploadProductPhoto(1L, multipartFile());

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(Map.of(PHOTO_FILE_NAME_KEY, PHOTO_FILE_NAME), result.getBody());
    }

    private static Stream<Arguments> unhappyPathParams() {
        return Stream.of(
                Arguments.of(
                        "ProductNotFoundException - HTTP 404",
                        new ProductNotFoundException(3L),
                        3L,
                        multipartFile(),
                        HttpStatus.NOT_FOUND,
                        "Termék a következő azonosítóval: #3 nem található"
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
                        "Hibás fájlformátum: .txt"
                ),
                Arguments.of(
                        "ProductAlreadyHasPhotoUploadedException - HTTP 400",
                        new ProductAlreadyHasPhotoUploadedException(1L),
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
    @MethodSource("unhappyPathParams")
    public void uploadProductPhotoTest_UnhappyPaths(
            String testCase,
            Exception exception,
            Long productId,
            MultipartFile multipartFile,
            HttpStatus httpStatus,
            String exceptionMessage
    ) {
        when(productPhotoService.uploadPhoto(any())).thenThrow(exception);

        var result = productPhotoController.uploadProductPhoto(productId, multipartFile);

        assertEquals(httpStatus, result.getStatusCode());
        assertEquals(exceptionMessage, result.getBody());
    }
}
