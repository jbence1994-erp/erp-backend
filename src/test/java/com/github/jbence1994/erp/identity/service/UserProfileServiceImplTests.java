package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.identity.dto.UserProfileCurrentAndNewPassword;
import com.github.jbence1994.erp.identity.exception.UserProfileCurrentPasswordAndPasswordNotMatchingException;
import com.github.jbence1994.erp.identity.exception.UserProfileNotFoundException;
import com.github.jbence1994.erp.identity.model.UserProfile;
import com.github.jbence1994.erp.identity.repository.UserProfileRepository;
import com.github.jbence1994.erp.identity.util.PasswordManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static com.github.jbence1994.erp.identity.constant.UserProfileTestConstants.USER_PROFILE_1_EMAIL;
import static com.github.jbence1994.erp.identity.constant.UserProfileTestConstants.USER_PROFILE_1_HASHED_NEW_PASSWORD;
import static com.github.jbence1994.erp.identity.constant.UserProfileTestConstants.USER_PROFILE_1_HASHED_PASSWORD;
import static com.github.jbence1994.erp.identity.constant.UserProfileTestConstants.USER_PROFILE_1_INVALID_PASSWORD;
import static com.github.jbence1994.erp.identity.constant.UserProfileTestConstants.USER_PROFILE_1_NEW_PASSWORD;
import static com.github.jbence1994.erp.identity.constant.UserProfileTestConstants.USER_PROFILE_1_PASSWORD;
import static com.github.jbence1994.erp.identity.testobject.UserProfileTestObject.userProfile1;
import static com.github.jbence1994.erp.identity.testobject.UserProfileTestObject.userProfile1IsDeleted;
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
class UserProfileServiceImplTests {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private PasswordManager passwordManager;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @Test
    public void getUserProfileTest_ById_HappyPath() {
        when(userProfileRepository.findById(any())).thenReturn(Optional.of(userProfile1()));

        var result = assertDoesNotThrow(() -> userProfileService.getUserProfile(1L));

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    private static Stream<Arguments> getUserProfileByIdUnhappyPathParams() {
        return Stream.of(
                Arguments.of(
                        "UserProfileNotFoundException - UserProfile not found by given id",
                        Optional.empty()
                ),
                Arguments.of(
                        "UserProfileNotFoundException - UserProfile is deleted",
                        Optional.of(userProfile1IsDeleted())
                )
        );
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("getUserProfileByIdUnhappyPathParams")
    public void getUserProfileTest_ById_UnhappyPaths(
            String testCase,
            Optional<UserProfile> userProfileOptional
    ) {
        when(userProfileRepository.findById(any())).thenReturn(userProfileOptional);

        assertThrows(
                UserProfileNotFoundException.class,
                () -> userProfileService.getUserProfile(1L)
        );

        verify(userProfileRepository, never()).save(any());
    }

    @Test
    public void getUserProfileTest_ByEmail_HappyPath() {
        when(userProfileRepository.findByEmail(any())).thenReturn(Optional.of(userProfile1()));

        var result = assertDoesNotThrow(() -> userProfileService.getUserProfile(USER_PROFILE_1_EMAIL));

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    private static Stream<Arguments> getUserProfileByEmailUnhappyPathParams() {
        return Stream.of(
                Arguments.of(
                        "UserProfileNotFoundException - UserProfile not found by given email",
                        Optional.empty()
                ),
                Arguments.of(
                        "UserProfileNotFoundException - UserProfile is deleted",
                        Optional.of(userProfile1IsDeleted())
                )
        );
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("getUserProfileByEmailUnhappyPathParams")
    public void getUserProfileTest_ByEmail_UnhappyPaths(
            String testCase,
            Optional<UserProfile> userProfileOptional
    ) {
        when(userProfileRepository.findByEmail(any())).thenReturn(userProfileOptional);

        assertThrows(
                UserProfileNotFoundException.class,
                () -> userProfileService.getUserProfile(USER_PROFILE_1_EMAIL)
        );

        verify(userProfileRepository, never()).save(any());
    }

    @Test
    public void createUserProfileTest_HappyPath() {
        when(passwordManager.encode(any())).thenReturn(USER_PROFILE_1_HASHED_PASSWORD);
        when(userProfileRepository.save(any())).thenReturn(userProfile1());

        var result = userProfileService.createUserProfile(userProfile1());

        assertNotNull(result);
        assertEquals(userProfile1().getId(), result.getId());
        assertEquals(userProfile1().getFirstName(), result.getFirstName());
        assertEquals(userProfile1().getLastName(), result.getLastName());
        assertEquals(userProfile1().getEmail(), result.getEmail());
        assertEquals(userProfile1().getPassword(), result.getPassword());
        assertEquals(userProfile1().getPhotoFileName(), result.getPhotoFileName());
        assertEquals(userProfile1().isDeleted(), result.isDeleted());
    }

    @Test
    public void updateUserProfileTest_HappyPath() {
        when(userProfileRepository.save(any())).thenReturn(userProfile1());

        userProfileService.updateUserProfile(userProfile1());

        verify(userProfileRepository, times(1)).save(any());
    }

    @Test
    public void updateUserProfilePasswordTest_HappyPath() {
        when(userProfileRepository.findById(any())).thenReturn(Optional.of(userProfile1()));
        when(passwordManager.verify(any(), any())).thenReturn(true);
        when(passwordManager.encode(any())).thenReturn(USER_PROFILE_1_HASHED_NEW_PASSWORD);

        assertDoesNotThrow(
                () -> userProfileService.updateUserProfilePassword(
                        1L,
                        new UserProfileCurrentAndNewPassword(USER_PROFILE_1_PASSWORD, USER_PROFILE_1_NEW_PASSWORD)
                )
        );

        verify(userProfileRepository, times(1)).save(any());
    }

    @Test
    public void updateUserProfilePasswordTest_UnhappyPath_UserProfileCurrentPasswordNotMatchingWithPassword() {
        when(userProfileRepository.findById(any())).thenReturn(Optional.of(userProfile1()));
        when(passwordManager.verify(any(), any())).thenReturn(false);

        assertThrows(
                UserProfileCurrentPasswordAndPasswordNotMatchingException.class,
                () -> userProfileService.updateUserProfilePassword(
                        1L,
                        new UserProfileCurrentAndNewPassword(USER_PROFILE_1_INVALID_PASSWORD, USER_PROFILE_1_NEW_PASSWORD)
                )
        );

        verify(userProfileRepository, never()).save(any());
    }

    @Test
    public void updateUserProfilePasswordTest_UnhappyPath_UserProfileNotFound() {
        when(userProfileRepository.findById(any())).thenReturn(Optional.of(userProfile1IsDeleted()));

        assertThrows(
                UserProfileNotFoundException.class,
                () -> userProfileService.updateUserProfilePassword(
                        1L,
                        new UserProfileCurrentAndNewPassword(USER_PROFILE_1_PASSWORD, USER_PROFILE_1_NEW_PASSWORD)
                )
        );

        verify(userProfileRepository, never()).save(any());
    }

    @Test
    public void deleteUserProfileTest_HappyPath() {
        when(userProfileRepository.findById(any())).thenReturn(Optional.of(userProfile1()));
        when(userProfileRepository.save(any())).thenReturn(userProfile1IsDeleted());

        assertDoesNotThrow(() -> userProfileService.deleteUserProfile(1L));

        verify(userProfileRepository, times(1)).save(any());
    }

    @Test
    public void deleteUserProfileTest_UnhappyPath_UserProfileAlreadyBeenDeleted() {
        when(userProfileRepository.findById(any())).thenReturn(Optional.of(userProfile1IsDeleted()));

        assertThrows(
                UserProfileNotFoundException.class,
                () -> userProfileService.deleteUserProfile(1L)
        );

        verify(userProfileRepository, never()).save(any());
    }

    @Test
    public void restoreUserProfileTest_HappyPath() {
        when(userProfileRepository.findById(any())).thenReturn(Optional.of(userProfile1IsDeleted()));
        when(userProfileRepository.save(any())).thenReturn(userProfile1());

        assertDoesNotThrow(() -> userProfileService.restoreUserProfile(1L));

        verify(userProfileRepository, times(1)).save(any());
    }

    @Test
    public void restoreUserProfileTest_UnhappyPath_UserProfileNotFoundByGivenId() {
        when(userProfileRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(
                UserProfileNotFoundException.class,
                () -> userProfileService.restoreUserProfile(1L)
        );
    }
}
