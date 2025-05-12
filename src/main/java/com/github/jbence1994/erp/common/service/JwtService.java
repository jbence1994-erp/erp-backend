package com.github.jbence1994.erp.common.service;

import com.github.jbence1994.erp.common.dto.UserIdentity;

public interface JwtService {
    Jwt generateAccessToken(UserIdentity identity);

    Jwt generateRefreshToken(UserIdentity identity);

    Jwt parseToken(String token);
}
