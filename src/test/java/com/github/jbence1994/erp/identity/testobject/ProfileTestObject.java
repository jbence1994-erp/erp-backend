package com.github.jbence1994.erp.identity.testobject;

import com.github.jbence1994.erp.identity.model.Profile;

import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.PHOTO_FILE_NAME;
import static com.github.jbence1994.erp.identity.constant.ProfileTestConstants.PROFILE_1_EMAIL;
import static com.github.jbence1994.erp.identity.constant.ProfileTestConstants.PROFILE_1_HASHED_PASSWORD;
import static com.github.jbence1994.erp.identity.testobject.UserTestObject.user1;

public final class ProfileTestObject {
    public static Profile profile1() {
        return new Profile(
                1L,
                PROFILE_1_EMAIL,
                PROFILE_1_HASHED_PASSWORD,
                null,
                false,
                user1()
        );
    }

    public static Profile profile1WithPhoto() {
        return new Profile(
                1L,
                PROFILE_1_EMAIL,
                PROFILE_1_HASHED_PASSWORD,
                PHOTO_FILE_NAME,
                false,
                user1()
        );
    }

    public static Profile profile1IsDeleted() {
        return new Profile(
                1L,
                PROFILE_1_EMAIL,
                PROFILE_1_HASHED_PASSWORD,
                null,
                true,
                user1()
        );
    }
}
