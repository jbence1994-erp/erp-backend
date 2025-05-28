package com.github.jbence1994.erp.common.service;

import com.github.jbence1994.erp.common.dto.UserIdentity;
import com.github.jbence1994.erp.common.model.Role;

public interface JwtService {
    Jwt generateAccessToken(UserIdentity identity);

    Jwt generateRefreshToken(UserIdentity identity);

    Jwt parseToken(String token);

    Long getUserIdFromToken(String token);

    Role getRoleFromToken(String token);
}
