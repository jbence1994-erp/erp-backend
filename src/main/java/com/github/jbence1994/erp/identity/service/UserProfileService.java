package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.identity.dto.UserProfileCurrentAndNewPassword;
import com.github.jbence1994.erp.identity.model.UserProfile;

public interface UserProfileService {
    UserProfile getUserProfile(Long id);

    UserProfile createUserProfile(UserProfile userProfile);

    void updateUserProfile(UserProfile userProfile);

    void updateUserProfilePassword(Long id, UserProfileCurrentAndNewPassword userProfileCurrentAndNewPassword);

    void deleteUserProfile(Long id);
}
