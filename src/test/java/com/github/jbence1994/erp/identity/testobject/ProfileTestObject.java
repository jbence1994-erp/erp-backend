package com.github.jbence1994.erp.identity.testobject;

import com.github.jbence1994.erp.identity.model.Profile;

import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.PHOTO_FILE_NAME;
import static com.github.jbence1994.erp.identity.constant.ProfileTestConstants.PROFILE_1_HASHED_PASSWORD;
import static com.github.jbence1994.erp.identity.constant.ProfileTestConstants.PROFILE_1_USERNAME;

public final class ProfileTestObject {
    public static Profile profile1() {
        return new Profile(
                1L,
                PROFILE_1_USERNAME,
                PROFILE_1_HASHED_PASSWORD,
                null,
                false,
                null // FIXME
        );
    }

    public static Profile profile1WithPhoto() {
        return new Profile(
                1L,
                PROFILE_1_USERNAME,
                PROFILE_1_HASHED_PASSWORD,
                PHOTO_FILE_NAME,
                false,
                null // FIXME
        );
    }
}
