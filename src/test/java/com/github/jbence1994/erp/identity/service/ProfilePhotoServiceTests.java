package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.common.util.FileUtils;
import com.github.jbence1994.erp.common.validation.FileValidator;
import com.github.jbence1994.erp.identity.dto.CreateProfilePhotoDto;
import com.github.jbence1994.erp.identity.exception.ProfileAlreadyHasPhotoUploadedException;
import com.github.jbence1994.erp.identity.exception.ProfilePhotoNotFoundException;
import com.github.jbence1994.erp.identity.exception.ProfilePhotoUploadException;
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
import static com.github.jbence1994.erp.identity.testobject.ProfileTestObject.profile1;
import static com.github.jbence1994.erp.identity.testobject.ProfileTestObject.profile1WithPhoto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfilePhotoServiceTests {

    @Mock
    private ProfileService profileService;

    @Mock
    private FileUtils fileUtils;

    @Mock
    private FileValidator fileValidator;

    @InjectMocks
    private ProfilePhotoService profilePhotoService;

    @Mock
    private CreateProfilePhotoDto createProfilePhotoDto;

    @BeforeEach
    public void setup() {
        when(profileService.getProfile(any())).thenReturn(profile1WithPhoto());
    }

    @Test
    public void getPhotoTest_HappyPath() throws IOException {
        when(fileUtils.read(any(), any())).thenReturn(new byte[FILE_SIZE]);

        var result = profilePhotoService.getPhoto(1L);

        assertTrue(result.getPhotoBytes().length > 0);
        assertEquals(JPEG, result.getFileExtension());
    }

    @Test
    public void getPhotoTest_UnhappyPath() throws IOException {
        when(fileUtils.read(any(), any())).thenThrow(new IOException());

        assertThrows(
                ProfilePhotoNotFoundException.class,
                () -> profilePhotoService.getPhoto(1L)
        );
    }

    @Test
    public void uploadPhotoTest_HappyPath() throws IOException {
        when(profileService.getProfile(any())).thenReturn(profile1());
        when(createProfilePhotoDto.createFileName()).thenReturn(PHOTO_FILE_NAME);
        doNothing().when(fileUtils).store(any(), any(), any());
        doNothing().when(profileService).updateProfile(any());

        var result = profilePhotoService.uploadPhoto(createProfilePhotoDto);

        assertFalse(result.isBlank());
    }

    @Test
    public void uploadPhotoTest_UnhappyPath_ProfileAlreadyHasPhotoUploaded() {
        assertThrows(
                ProfileAlreadyHasPhotoUploadedException.class,
                () -> profilePhotoService.uploadPhoto(createProfilePhotoDto)
        );
    }

    @Test
    public void uploadPhotoTest_UnhappyPath_ProfilePhotoUploadFailure() throws IOException {
        when(profileService.getProfile(any())).thenReturn(profile1());
        doThrow(new IOException("Disk error")).when(fileUtils).store(any(), any(), any());

        assertThrows(
                ProfilePhotoUploadException.class,
                () -> profilePhotoService.uploadPhoto(createProfilePhotoDto)
        );
    }
}
