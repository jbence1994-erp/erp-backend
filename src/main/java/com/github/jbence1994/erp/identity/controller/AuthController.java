package com.github.jbence1994.erp.identity.controller;

import com.github.jbence1994.erp.common.config.JwtConfig;
import com.github.jbence1994.erp.common.service.JwtService;
import com.github.jbence1994.erp.identity.dto.JwtResponse;
import com.github.jbence1994.erp.identity.dto.LoginRequest;
import com.github.jbence1994.erp.identity.mapper.UserProfileToUserIdentityMapper;
import com.github.jbence1994.erp.identity.service.UserProfileService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserProfileService userProfileService;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final UserProfileToUserIdentityMapper toUserIdentityMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var userProfile = userProfileService.getUserProfile(request.email());
        var userIdentity = toUserIdentityMapper.toUserIdentity(userProfile);

        var accessToken = jwtService.generateAccessToken(userIdentity);
        var refreshToken = jwtService.generateRefreshToken(userIdentity);

        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new JwtResponse(accessToken.toString()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(
            @CookieValue("refreshToken") String refreshToken
    ) {
        var jwt = jwtService.parseToken(refreshToken);
        if (jwt == null || jwt.isExpired()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        var userProfile = userProfileService.getUserProfile(jwt.getUserId());
        var accessToken = jwtService.generateAccessToken(toUserIdentityMapper.toUserIdentity(userProfile));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new JwtResponse(accessToken.toString()));
    }
}
