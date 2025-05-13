package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.identity.repository.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.github.jbence1994.erp.identity.constant.UserProfileTestConstants.USER_PROFILE_1_EMAIL;
import static com.github.jbence1994.erp.identity.testobject.UserProfileTestObject.userProfile1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void loadUserByUsername_HappyPath() {
        when(userProfileRepository.findByEmail(any())).thenReturn(Optional.of(userProfile1()));

        var result = userService.loadUserByUsername(USER_PROFILE_1_EMAIL);

        assertEquals(userProfile1().getEmail(), result.getUsername());
        assertEquals(userProfile1().getPassword(), result.getPassword());
        assertTrue(result.getAuthorities().isEmpty());

        verify(userProfileRepository).findByEmail(any());
    }

    @Test
    void loadUserByUsername_UnhappyPath() {
        when(userProfileRepository.findByEmail(any())).thenReturn(Optional.empty());

        var result = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(USER_PROFILE_1_EMAIL));

        assertEquals(USER_PROFILE_1_EMAIL, result.getMessage());

        verify(userProfileRepository).findByEmail(any());
    }
}
