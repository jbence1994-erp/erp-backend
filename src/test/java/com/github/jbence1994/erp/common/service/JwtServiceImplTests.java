package com.github.jbence1994.erp.common.service;

import com.github.jbence1994.erp.common.config.JwtConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static com.github.jbence1994.erp.common.constant.AuthTestConstants.ACCESS_TOKEN_EXPIRATION;
import static com.github.jbence1994.erp.common.constant.AuthTestConstants.MALFORMED_TOKEN;
import static com.github.jbence1994.erp.common.constant.AuthTestConstants.REFRESH_TOKEN_EXPIRATION;
import static com.github.jbence1994.erp.common.testobject.SecretKeyTestObject.secretKey;
import static com.github.jbence1994.erp.common.testobject.UserIdentityTestObject.userIdentity1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JwtServiceImplTests {

    @Mock
    private JwtConfig jwtConfig;

    @InjectMocks
    private JwtServiceImpl jwtService;

    @BeforeEach
    public void setup() {
        when(jwtConfig.getAccessTokenExpiration()).thenReturn(ACCESS_TOKEN_EXPIRATION);
        when(jwtConfig.getRefreshTokenExpiration()).thenReturn(REFRESH_TOKEN_EXPIRATION);
        when(jwtConfig.getSecretKey()).thenReturn(secretKey());
    }

    @Test
    public void generateAccessTokenTest() {
        var jwt = jwtService.generateAccessToken(userIdentity1());

        assertNotNull(jwt);

        var parsed = jwtService.parseToken(jwt.toString());

        assertNotNull(parsed);
        assertEquals(userIdentity1().id(), parsed.getUserId());
        assertFalse(parsed.isExpired());

        verify(jwtConfig).getAccessTokenExpiration();
        verify(jwtConfig, atLeastOnce()).getSecretKey();
    }

    @Test
    public void generateRefreshTokenTest() {
        var jwt = jwtService.generateRefreshToken(userIdentity1());

        assertNotNull(jwt);

        var parsed = jwtService.parseToken(jwt.toString());

        assertNotNull(parsed);
        assertEquals(userIdentity1().id(), parsed.getUserId());
        assertFalse(parsed.isExpired());

        verify(jwtConfig).getRefreshTokenExpiration();
        verify(jwtConfig, atLeastOnce()).getSecretKey();
    }

    @Test
    public void parseTokenTest_HappyPath() {
        var token = jwtService.generateAccessToken(userIdentity1()).toString();
        var result = jwtService.parseToken(token);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
    }

    @Test
    public void parseTokenTest_UnhappyPath_MalformedToken() {
        var result = jwtService.parseToken(MALFORMED_TOKEN);

        assertNull(result);
    }
}
