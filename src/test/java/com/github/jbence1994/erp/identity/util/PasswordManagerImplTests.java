package com.github.jbence1994.erp.identity.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.github.jbence1994.erp.identity.constant.UserProfileTestConstants.USER_PROFILE_1_HASHED_PASSWORD;
import static com.github.jbence1994.erp.identity.constant.UserProfileTestConstants.USER_PROFILE_1_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordManagerImplTests {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordManagerImpl passwordManager;

    @Test
    public void encodeTest() {
        when(passwordEncoder.encode(any())).thenReturn(USER_PROFILE_1_HASHED_PASSWORD);

        var result = passwordManager.encode(USER_PROFILE_1_PASSWORD);

        assertEquals(USER_PROFILE_1_HASHED_PASSWORD, result);
    }

    @Test
    public void verifyTest() {
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        var result = passwordManager.verify(USER_PROFILE_1_PASSWORD, USER_PROFILE_1_HASHED_PASSWORD);

        assertTrue(result);
    }

    @Test
    public void verifyTest_UnhappyPath() {
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        var result = passwordManager.verify(USER_PROFILE_1_PASSWORD, USER_PROFILE_1_HASHED_PASSWORD);

        assertFalse(result);
    }
}
