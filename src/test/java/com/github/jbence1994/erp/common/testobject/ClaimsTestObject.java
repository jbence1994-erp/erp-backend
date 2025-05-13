package com.github.jbence1994.erp.common.testobject;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import static com.github.jbence1994.erp.common.testobject.UserIdentityTestObject.userIdentity1;

public final class ClaimsTestObject {
    public static Claims claims() {
        return Jwts.claims()
                .subject(userIdentity1().id().toString())
                .add("email", userIdentity1().email())
                .add("firstName", userIdentity1().firstName())
                .add("lastName", userIdentity1().lastName())
                .build();
    }
}
