package com.github.jbence1994.erp.identity.controller;

import com.github.jbence1994.erp.common.dto.PhotoResponse;
import com.github.jbence1994.erp.common.exception.EmptyFileException;
import com.github.jbence1994.erp.common.exception.InvalidFileExtensionException;
import com.github.jbence1994.erp.identity.exception.ProfileAlreadyHasPhotoUploadedException;
import com.github.jbence1994.erp.identity.exception.ProfileNotFoundException;
import com.github.jbence1994.erp.identity.exception.ProfilePhotoNotFoundException;
import com.github.jbence1994.erp.identity.exception.ProfilePhotoUploadException;
import com.github.jbence1994.erp.identity.mapper.MultipartFileToCreateProfilePhotoDtoMapper;
import com.github.jbence1994.erp.identity.service.ProfilePhotoService;
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
import static com.github.jbence1994.erp.identity.testobject.ProfilePhotoDtoTestObject.profilePhotoDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfilePhotoControllerTests {

    @Mock
    private ProfilePhotoService profilePhotoService;

    @Mock
    private MultipartFileToCreateProfilePhotoDtoMapper toCreateProfilePhotoDtoMapper;

    @InjectMocks
    private ProfilePhotoController profilePhotoController;

    @Test
    public void getProfilePhotoTest_HappyPath() {
        when(profilePhotoService.getPhoto(any())).thenReturn(profilePhotoDto());

        var result = profilePhotoController.getProfilePhoto(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(IMAGE, result.getHeaders().getContentType().getType());
        assertEquals(JPEG, result.getHeaders().getContentType().getSubtype());
        assertInstanceOf(byte[].class, result.getBody());
    }

    @Test
    public void getProfilePhotoTest_UnhappyPath_ProfilePhotoNotFound() {
        when(profilePhotoService.getPhoto(any())).thenThrow(new ProfilePhotoNotFoundException(3L));

        var result = profilePhotoController.getProfilePhoto(3L);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Felhasználói fiók a következő azonosítóval: #3 nem rendelkezik feltöltött fényképpel", result.getBody().toString());
    }

    @Test
    public void uploadProfilePhotoTest_HappyPath() {
        when(profilePhotoService.uploadPhoto(any())).thenReturn(PHOTO_FILE_NAME);

        var result = profilePhotoController.uploadProfilePhoto(1L, multipartFile());

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(new PhotoResponse(PHOTO_FILE_NAME), result.getBody());
    }

    static Stream<Arguments> unhappyPathParams() {
        return Stream.of(
                Arguments.of(
                        "ProfileNotFoundException - HTTP 404",
                        new ProfileNotFoundException(3L),
                        3L,
                        multipartFile(),
                        HttpStatus.NOT_FOUND,
                        "Felhasználói fiók a következő azonosítóval: #3 nem található"
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
                        "ProfileAlreadyHasPhotoUploadedException - HTTP 400",
                        new ProfileAlreadyHasPhotoUploadedException(1L),
                        1L,
                        multipartFile(),
                        HttpStatus.BAD_REQUEST,
                        "Felhasználói fiók a következő azonosítóval: #1 már rendelkezik feltöltött fényképpel"
                ),
                Arguments.of(
                        "ProfilePhotoUploadException - HTTP 500",
                        new ProfilePhotoUploadException(1L),
                        1L,
                        multipartFile(),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Fénykép feltöltése az alábbi felhasználói fiókhoz: #1 sikeretelen volt"
                )
        );
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("unhappyPathParams")
    public void uploadProfilePhotoTest_UnhappyPaths(
            String testCase,
            Exception exception,
            Long profileId,
            MultipartFile multipartFile,
            HttpStatus httpStatus,
            String exceptionMessage
    ) {
        when(profilePhotoService.uploadPhoto(any())).thenThrow(exception);

        var result = profilePhotoController.uploadProfilePhoto(profileId, multipartFile);

        assertEquals(httpStatus, result.getStatusCode());
        assertEquals(exceptionMessage, result.getBody());
    }
}
