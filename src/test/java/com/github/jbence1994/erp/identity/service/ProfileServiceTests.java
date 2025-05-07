package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.identity.exception.ProfileNotFoundException;
import com.github.jbence1994.erp.identity.repository.ProfileRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.jbence1994.erp.identity.testobject.ProfileTestObject.profile1;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProfileServiceTests {
    private final ProfileRepository profileRepository = mock(ProfileRepository.class);
    private final ProfileService profileService = new ProfileService(profileRepository);

    @Test
    public void getProfileTest_HappyPath() {
        when(profileRepository.findById(any())).thenReturn(Optional.of(profile1()));

        var result = assertDoesNotThrow(() -> profileService.getProfile(1L));

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void getProfileTest_UnhappyPath() {
        when(profileRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(
                ProfileNotFoundException.class,
                () -> profileService.getProfile(3L)
        );
    }

    @Test
    public void updateProfileTest_HappyPath() {
        when(profileRepository.save(any())).thenReturn(profile1());

        profileService.updateProfile(profile1());

        verify(profileRepository, times(1)).save(any());
    }
}
