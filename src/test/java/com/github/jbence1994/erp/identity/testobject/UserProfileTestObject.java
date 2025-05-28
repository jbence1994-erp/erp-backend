package com.github.jbence1994.erp.identity.testobject;

import com.github.jbence1994.erp.common.model.Role;
import com.github.jbence1994.erp.identity.model.UserProfile;

import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.PHOTO_FILE_NAME;
import static com.github.jbence1994.erp.identity.constant.UserProfileTestConstants.USER_PROFILE_1_EMAIL;
import static com.github.jbence1994.erp.identity.constant.UserProfileTestConstants.USER_PROFILE_1_FIRST_NAME;
import static com.github.jbence1994.erp.identity.constant.UserProfileTestConstants.USER_PROFILE_1_HASHED_NEW_PASSWORD;
import static com.github.jbence1994.erp.identity.constant.UserProfileTestConstants.USER_PROFILE_1_LAST_NAME;

public final class UserProfileTestObject {
    public static UserProfile userProfile1() {
        return new UserProfile(
                1L,
                USER_PROFILE_1_FIRST_NAME,
                USER_PROFILE_1_LAST_NAME,
                USER_PROFILE_1_EMAIL,
                USER_PROFILE_1_HASHED_NEW_PASSWORD,
                null,
                Role.USER,
                false
        );
    }

    public static UserProfile userProfile1WithPhoto() {
        return new UserProfile(
                1L,
                USER_PROFILE_1_FIRST_NAME,
                USER_PROFILE_1_LAST_NAME,
                USER_PROFILE_1_EMAIL,
                USER_PROFILE_1_HASHED_NEW_PASSWORD,
                PHOTO_FILE_NAME,
                Role.USER,
                false
        );
    }

    public static UserProfile userProfile1IsDeleted() {
        return new UserProfile(
                1L,
                USER_PROFILE_1_FIRST_NAME,
                USER_PROFILE_1_LAST_NAME,
                USER_PROFILE_1_EMAIL,
                USER_PROFILE_1_HASHED_NEW_PASSWORD,
                null,
                Role.USER,
                true
        );
    }
}
