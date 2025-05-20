package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.common.util.FileUtils;
import com.github.jbence1994.erp.common.validation.FileValidator;
import com.github.jbence1994.erp.identity.dto.CreateUserProfilePhotoDto;
import com.github.jbence1994.erp.identity.exception.UserProfileAlreadyHasAPhotoUploadedException;
import com.github.jbence1994.erp.identity.exception.UserProfileDoesNotHaveAPhotoUploadedYetException;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoDeleteException;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoDownloadException;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoNotFoundException;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoUploadException;
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
import static com.github.jbence1994.erp.identity.testobject.UserProfileTestObject.userProfile1;
import static com.github.jbence1994.erp.identity.testobject.UserProfileTestObject.userProfile1WithPhoto;
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
class UserProfilePhotoServiceTests {

    @Mock
    private UserProfileService userProfileService;

    @Mock
    private FileUtils fileUtils;

    @Mock
    private FileValidator fileValidator;

    @InjectMocks
    private UserProfilePhotoService userProfilePhotoService;

    @Mock
    private CreateUserProfilePhotoDto createUserProfilePhotoDto;

    @BeforeEach
    public void setup() {
        when(userProfileService.getUserProfile(any())).thenReturn(userProfile1WithPhoto());
    }

    @Test
    public void uploadPhotoTest_HappyPath() throws IOException {
        when(userProfileService.getUserProfile(any())).thenReturn(userProfile1());
        when(createUserProfilePhotoDto.createFileName()).thenReturn(PHOTO_FILE_NAME);
        doNothing().when(fileUtils).store(any(), any(), any());
        doNothing().when(userProfileService).updateUserProfile(any());

        var result = userProfilePhotoService.uploadPhoto(createUserProfilePhotoDto);

        assertFalse(result.isBlank());
    }

    @Test
    public void uploadPhotoTest_UnhappyPath_UserProfileAlreadyHasAPhotoUploaded() {
        assertThrows(
                UserProfileAlreadyHasAPhotoUploadedException.class,
                () -> userProfilePhotoService.uploadPhoto(createUserProfilePhotoDto)
        );
    }

    @Test
    public void uploadPhotoTest_UnhappyPath_IOException() throws IOException {
        when(userProfileService.getUserProfile(any())).thenReturn(userProfile1());
        doThrow(new IOException("Disk error")).when(fileUtils).store(any(), any(), any());

        assertThrows(
                UserProfilePhotoUploadException.class,
                () -> userProfilePhotoService.uploadPhoto(createUserProfilePhotoDto)
        );
    }

    @Test
    public void getPhotoTest_HappyPath() throws IOException {
        when(fileUtils.read(any(), any())).thenReturn(new byte[FILE_SIZE]);

        var result = userProfilePhotoService.getPhoto(1L);

        assertTrue(result.getPhotoBytes().length > 0);
        assertEquals(JPEG, result.getFileExtension());
    }

    @Test
    public void getPhotoTest_UnhappyPath_UserProfileHasNoPhoto() {
        when(userProfileService.getUserProfile(any())).thenReturn(userProfile1());

        assertThrows(
                UserProfilePhotoNotFoundException.class,
                () -> userProfilePhotoService.getPhoto(1L)
        );
    }

    @Test
    public void getPhotoTest_UnhappyPath_IOException() throws IOException {
        when(fileUtils.read(any(), any())).thenThrow(new IOException("Disk error"));

        assertThrows(
                UserProfilePhotoDownloadException.class,
                () -> userProfilePhotoService.getPhoto(1L)
        );
    }

    @Test
    public void deletePhotoTest_HappyPath() throws IOException {
        doNothing().when(fileUtils).delete(any(), any());

        assertDoesNotThrow(() -> userProfilePhotoService.deletePhoto(1L));
    }

    @Test
    public void deletePhotoTest_UnhappyPath_UserProfileDoesNotHaveAPhotoUploadedYetException() {
        when(userProfileService.getUserProfile(any())).thenReturn(userProfile1());

        assertThrows(
                UserProfileDoesNotHaveAPhotoUploadedYetException.class,
                () -> userProfilePhotoService.deletePhoto(1L)
        );
    }

    @Test
    public void deletePhotoTest_UnhappyPath_IOException() throws IOException {
        doThrow(new IOException("Disk error")).when(fileUtils).delete(any(), any());

        assertThrows(
                UserProfilePhotoDeleteException.class,
                () -> userProfilePhotoService.deletePhoto(1L)
        );
    }
}
