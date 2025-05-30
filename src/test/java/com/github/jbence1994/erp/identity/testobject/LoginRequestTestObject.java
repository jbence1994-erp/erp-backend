package com.github.jbence1994.erp.identity.testobject;

import com.github.jbence1994.erp.identity.dto.LoginRequest;

import static com.github.jbence1994.erp.identity.constant.UserProfileTestConstants.USER_PROFILE_1_EMAIL;
import static com.github.jbence1994.erp.identity.constant.UserProfileTestConstants.USER_PROFILE_1_INVALID_PASSWORD;
import static com.github.jbence1994.erp.identity.constant.UserProfileTestConstants.USER_PROFILE_1_PASSWORD;

public final class LoginRequestTestObject {
    public static LoginRequest loginRequest() {
        return new LoginRequest(
                USER_PROFILE_1_EMAIL,
                USER_PROFILE_1_PASSWORD
        );
    }

    public static LoginRequest invalidLoginRequest() {
        return new LoginRequest(
                USER_PROFILE_1_EMAIL,
                USER_PROFILE_1_INVALID_PASSWORD
        );
    }
}
