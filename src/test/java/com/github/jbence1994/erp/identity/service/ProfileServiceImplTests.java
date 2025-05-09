package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.identity.dto.CurrentAndNewPassword;
import com.github.jbence1994.erp.identity.exception.CurrentPasswordAndPasswordNotMatchingException;
import com.github.jbence1994.erp.identity.exception.ProfileAlreadyExistException;
import com.github.jbence1994.erp.identity.exception.ProfileNotFoundException;
import com.github.jbence1994.erp.identity.repository.ProfileRepository;
import com.github.jbence1994.erp.identity.util.PasswordManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.github.jbence1994.erp.identity.constant.ProfileTestConstants.PROFILE_1_HASHED_NEW_PASSWORD;
import static com.github.jbence1994.erp.identity.constant.ProfileTestConstants.PROFILE_1_HASHED_PASSWORD;
import static com.github.jbence1994.erp.identity.constant.ProfileTestConstants.PROFILE_1_INVALID_PASSWORD;
import static com.github.jbence1994.erp.identity.constant.ProfileTestConstants.PROFILE_1_NEW_PASSWORD;
import static com.github.jbence1994.erp.identity.constant.ProfileTestConstants.PROFILE_1_PASSWORD;
import static com.github.jbence1994.erp.identity.testobject.ProfileTestObject.profile1;
import static com.github.jbence1994.erp.identity.testobject.ProfileTestObject.profile1IsDeleted;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTests {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PasswordManager passwordManager;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Test
    public void getProfileTest_HappyPath() {
        when(profileRepository.findById(any())).thenReturn(Optional.of(profile1()));

        var result = assertDoesNotThrow(() -> profileService.getProfile(1L));

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void getProfileTest_UnhappyPath_ProfileNotFoundByGivenId() {
        when(profileRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(
                ProfileNotFoundException.class,
                () -> profileService.getProfile(5L)
        );
    }

    @Test
    public void getProfileTest_UnhappyPath_ProfileNotFound() {
        when(profileRepository.findById(any())).thenReturn(Optional.of(profile1IsDeleted()));

        assertThrows(
                ProfileNotFoundException.class,
                () -> profileService.deleteProfile(1L)
        );

        verify(profileRepository, never()).save(any());
    }

    @Test
    public void createProfileTest_HappyPath() {
        when(profileRepository.findByUserId(any())).thenReturn(Optional.empty());
        when(passwordManager.encode(any())).thenReturn(PROFILE_1_HASHED_PASSWORD);
        when(profileRepository.save(any())).thenReturn(profile1());

        var result = profileService.createProfile(profile1());

        assertNotNull(result);
        assertEquals(profile1().getId(), result.getId());
        assertEquals(profile1().getUsername(), result.getUsername());
        assertEquals(profile1().getPassword(), result.getPassword());
        assertEquals(profile1().getPhotoFileName(), result.getPhotoFileName());
        assertEquals(profile1().isDeleted(), result.isDeleted());
        assertEquals(profile1().getUser().getId(), result.getUser().getId());
        assertEquals(profile1().getUser().getFirstName(), result.getUser().getFirstName());
        assertEquals(profile1().getUser().getLastName(), result.getUser().getLastName());
        assertEquals(profile1().getUser().getEmail(), result.getUser().getEmail());
    }

    @Test
    public void createProfileTest_UnhappyPath_ProfileAlreadyBeenCreatedForGivenUser() {
        when(profileRepository.findByUserId(any())).thenReturn(Optional.of(profile1()));

        assertThrows(
                ProfileAlreadyExistException.class,
                () -> profileService.createProfile(profile1())
        );
    }

    @Test
    public void updateProfileTest_HappyPath() {
        when(profileRepository.save(any())).thenReturn(profile1());

        profileService.updateProfile(profile1());

        verify(profileRepository, times(1)).save(any());
    }

    @Test
    public void updateProfilePasswordTest_HappyPath() {
        when(profileRepository.findById(any())).thenReturn(Optional.of(profile1()));
        when(passwordManager.verify(any(), any())).thenReturn(true);
        when(passwordManager.encode(any())).thenReturn(PROFILE_1_HASHED_NEW_PASSWORD);

        assertDoesNotThrow(
                () -> profileService.updateProfilePassword(
                        1L,
                        new CurrentAndNewPassword(PROFILE_1_PASSWORD, PROFILE_1_NEW_PASSWORD)
                )
        );

        verify(profileRepository, times(1)).save(any());
    }

    @Test
    public void updateProfilePasswordTest_UnhappyPath_CurrentPasswordNotMatchingWithPassword() {
        when(profileRepository.findById(any())).thenReturn(Optional.of(profile1()));
        when(passwordManager.verify(any(), any())).thenReturn(false);

        assertThrows(
                CurrentPasswordAndPasswordNotMatchingException.class,
                () -> profileService.updateProfilePassword(
                        1L,
                        new CurrentAndNewPassword(PROFILE_1_INVALID_PASSWORD, PROFILE_1_NEW_PASSWORD)
                )
        );

        verify(profileRepository, never()).save(any());
    }

    @Test
    public void updateProfilePasswordTest_UnhappyPath_ProfileNotFound() {
        when(profileRepository.findById(any())).thenReturn(Optional.of(profile1IsDeleted()));

        assertThrows(
                ProfileNotFoundException.class,
                () -> profileService.updateProfilePassword(
                        1L,
                        new CurrentAndNewPassword(PROFILE_1_PASSWORD, PROFILE_1_NEW_PASSWORD)
                )
        );

        verify(profileRepository, never()).save(any());
    }

    @Test
    public void deleteProfileTest_HappyPath() {
        when(profileRepository.findById(any())).thenReturn(Optional.of(profile1()));
        when(profileRepository.save(any())).thenReturn(profile1IsDeleted());

        assertDoesNotThrow(() -> profileService.deleteProfile(1L));

        verify(profileRepository, times(1)).save(any());
    }

    @Test
    public void deleteProfileTest_UnhappyPath_ProfileAlreadyBeenDeleted() {
        when(profileRepository.findById(any())).thenReturn(Optional.of(profile1IsDeleted()));

        assertThrows(
                ProfileNotFoundException.class,
                () -> profileService.deleteProfile(1L)
        );

        verify(profileRepository, never()).save(any());
    }
}
