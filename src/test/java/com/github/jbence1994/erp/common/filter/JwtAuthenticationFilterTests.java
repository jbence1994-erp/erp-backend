package com.github.jbence1994.erp.common.filter;

import com.github.jbence1994.erp.common.service.Jwt;
import com.github.jbence1994.erp.common.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static com.github.jbence1994.erp.common.constant.AuthTestConstants.BEARER_TOKEN;
import static com.github.jbence1994.erp.common.constant.AuthTestConstants.INVALID_BEARER_TOKEN;
import static com.github.jbence1994.erp.common.constant.AuthTestConstants.MALFORMED_BEARER_TOKEN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTests {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void doFilterInternalTest_HappyPath() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(BEARER_TOKEN);

        var parsedJwt = mock(Jwt.class);
        when(jwtService.parseToken(any())).thenReturn(parsedJwt);
        when(parsedJwt.isExpired()).thenReturn(false);
        when(parsedJwt.getUserId()).thenReturn(1L);

        filter.doFilterInternal(request, response, filterChain);

        verify(jwtService).parseToken(any());
        verify(parsedJwt).isExpired();

        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(auth.getPrincipal()).isEqualTo(1L);
        assertThat(auth.getCredentials()).isNull();
        assertTrue(auth.getAuthorities().isEmpty());

        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void doFilterInternalTest_UnhappyPath_NoAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    public void doFilterInternalTest_UnhappyPath_MalformedAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(MALFORMED_BEARER_TOKEN);

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    public void doFilterInternalTest_UnhappyPath_parseTokenFailure() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(INVALID_BEARER_TOKEN);

        when(jwtService.parseToken(any())).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        verify(jwtService).parseToken(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void doFilterInternalTest_UnhappyPath_TokenIsExpired() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(BEARER_TOKEN);

        var parsedJwt = mock(Jwt.class);
        when(jwtService.parseToken(any())).thenReturn(parsedJwt);
        when(parsedJwt.isExpired()).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        verify(jwtService).parseToken(any());
        verify(parsedJwt).isExpired();
        verify(filterChain).doFilter(request, response);
    }
}
