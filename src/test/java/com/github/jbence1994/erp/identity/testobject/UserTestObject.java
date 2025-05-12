package com.github.jbence1994.erp.identity.testobject;

import com.github.jbence1994.erp.identity.model.User;

import static com.github.jbence1994.erp.identity.constant.UserTestConstants.USER_1_FIRST_NAME;
import static com.github.jbence1994.erp.identity.constant.UserTestConstants.USER_1_LAST_NAME;

public final class UserTestObject {
    public static User user1() {
        return new User(
                1L,
                USER_1_FIRST_NAME,
                USER_1_LAST_NAME
        );
    }
}
