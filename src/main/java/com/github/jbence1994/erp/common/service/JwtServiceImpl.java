package com.github.jbence1994.erp.common.service;

import com.github.jbence1994.erp.common.config.JwtConfig;
import com.github.jbence1994.erp.common.dto.UserIdentity;
import com.github.jbence1994.erp.common.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtConfig jwtConfig;

    @Override
    public Jwt generateAccessToken(UserIdentity identity) {
        return generateToken(identity, jwtConfig.getAccessTokenExpiration());
    }

    @Override
    public Jwt generateRefreshToken(UserIdentity identity) {
        return generateToken(identity, jwtConfig.getRefreshTokenExpiration());
    }

    @Override
    public Jwt parseToken(String token) {
        try {
            var claims = getClaims(token);
            return new Jwt(claims, jwtConfig.getSecretKey());
        } catch (JwtException exception) {
            return null;
        }
    }

    @Override
    public Long getUserIdFromToken(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }

    @Override
    public Role getRoleFromToken(String token) {
        return Role.valueOf(getClaims(token).get("role", String.class));
    }

    private Jwt generateToken(UserIdentity identity, long tokenExpiration) {
        var claims = Jwts
                .claims()
                .subject(identity.id().toString())
                .add("email", identity.email())
                .add("firstName", identity.firstName())
                .add("lastName", identity.lastName())
                .add("role", identity.role())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .build();

        return new Jwt(claims, jwtConfig.getSecretKey());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
