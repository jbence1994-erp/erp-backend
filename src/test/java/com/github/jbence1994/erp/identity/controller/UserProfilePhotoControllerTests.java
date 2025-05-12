package com.github.jbence1994.erp.identity.controller;

import com.github.jbence1994.erp.common.dto.PhotoResponse;
import com.github.jbence1994.erp.common.exception.EmptyFileException;
import com.github.jbence1994.erp.common.exception.InvalidFileExtensionException;
import com.github.jbence1994.erp.common.mapper.MultipartFileToCreatePhotoDtoMapper;
import com.github.jbence1994.erp.identity.dto.CreateUserProfilePhotoDto;
import com.github.jbence1994.erp.identity.exception.UserProfileAlreadyHasPhotoUploadedException;
import com.github.jbence1994.erp.identity.exception.UserProfileNotFoundException;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoNotFoundException;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoUploadException;
import com.github.jbence1994.erp.identity.service.UserProfilePhotoService;
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
import static com.github.jbence1994.erp.identity.testobject.UserProfilePhotoDtoTestObject.userProfilePhotoDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfilePhotoControllerTests {

    @Mock
    private UserProfilePhotoService userProfilePhotoService;

    @Mock
    private MultipartFileToCreatePhotoDtoMapper<CreateUserProfilePhotoDto> toCreatePhotoDtoMapper;

    @InjectMocks
    private UserProfilePhotoController userProfilePhotoController;

    @Test
    public void getUserProfilePhotoTest_HappyPath() {
        when(userProfilePhotoService.getPhoto(any())).thenReturn(userProfilePhotoDto());

        var result = userProfilePhotoController.getUserProfilePhoto(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(IMAGE, result.getHeaders().getContentType().getType());
        assertEquals(JPEG, result.getHeaders().getContentType().getSubtype());
        assertInstanceOf(byte[].class, result.getBody());
    }

    @Test
    public void getUserProfilePhotoTest_UnhappyPath_UserProfilePhotoNotFound() {
        when(userProfilePhotoService.getPhoto(any())).thenThrow(new UserProfilePhotoNotFoundException(3L));

        var result = userProfilePhotoController.getUserProfilePhoto(3L);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Felhasználói fiók a következő azonosítóval: #3 nem rendelkezik feltöltött fényképpel", result.getBody().toString());
    }

    @Test
    public void uploadUserProfilePhotoTest_HappyPath() {
        when(userProfilePhotoService.uploadPhoto(any())).thenReturn(PHOTO_FILE_NAME);

        var result = userProfilePhotoController.uploadUserProfilePhoto(1L, multipartFile());

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(new PhotoResponse(PHOTO_FILE_NAME), result.getBody());
    }

    static Stream<Arguments> unhappyPathParams() {
        return Stream.of(
                Arguments.of(
                        "UserProfileNotFoundException - HTTP 404",
                        new UserProfileNotFoundException(3L),
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
                        "UserProfileAlreadyHasPhotoUploadedException - HTTP 400",
                        new UserProfileAlreadyHasPhotoUploadedException(1L),
                        1L,
                        multipartFile(),
                        HttpStatus.BAD_REQUEST,
                        "Felhasználói fiók a következő azonosítóval: #1 már rendelkezik feltöltött fényképpel"
                ),
                Arguments.of(
                        "UserProfilePhotoUploadException - HTTP 500",
                        new UserProfilePhotoUploadException(1L),
                        1L,
                        multipartFile(),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Fénykép feltöltése az alábbi felhasználói fiókhoz: #1 sikeretelen volt"
                )
        );
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("unhappyPathParams")
    public void uploadUserProfilePhotoTest_UnhappyPaths(
            String testCase,
            Exception exception,
            Long userProfileId,
            MultipartFile multipartFile,
            HttpStatus httpStatus,
            String exceptionMessage
    ) {
        when(userProfilePhotoService.uploadPhoto(any())).thenThrow(exception);

        var result = userProfilePhotoController.uploadUserProfilePhoto(userProfileId, multipartFile);

        assertEquals(httpStatus, result.getStatusCode());
        assertEquals(exceptionMessage, result.getBody());
    }
}
