package com.github.jbence1994.erp.common.testobject;

import com.github.jbence1994.erp.common.dto.UserIdentity;
import com.github.jbence1994.erp.common.model.Role;

import static com.github.jbence1994.erp.common.constant.UserIdentityTestConstants.USER_IDENTITY_1_EMAIL;
import static com.github.jbence1994.erp.common.constant.UserIdentityTestConstants.USER_IDENTITY_1_FIRST_NAME;
import static com.github.jbence1994.erp.common.constant.UserIdentityTestConstants.USER_IDENTITY_1_LAST_NAME;

public final class UserIdentityTestObject {
    public static UserIdentity userIdentity1() {
        return new UserIdentity(
                1L,
                USER_IDENTITY_1_EMAIL,
                USER_IDENTITY_1_FIRST_NAME,
                USER_IDENTITY_1_LAST_NAME,
                Role.ADMIN
        );
    }
}
