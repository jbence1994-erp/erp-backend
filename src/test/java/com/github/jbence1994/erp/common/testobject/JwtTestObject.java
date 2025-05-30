package com.github.jbence1994.erp.common.testobject;

import com.github.jbence1994.erp.common.service.Jwt;

import static com.github.jbence1994.erp.common.testobject.ClaimsTestObject.claims;
import static com.github.jbence1994.erp.common.testobject.SecretKeyTestObject.secretKey;

public final class JwtTestObject {

    public static Jwt accessToken() {
        return new Jwt(claims(), secretKey());
    }

    public static Jwt refreshToken() {
        return new Jwt(claims(), secretKey());
    }
}
