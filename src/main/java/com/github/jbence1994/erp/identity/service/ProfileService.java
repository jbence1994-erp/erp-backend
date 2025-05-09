package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.identity.dto.CurrentAndNewPassword;
import com.github.jbence1994.erp.identity.model.Profile;

public interface ProfileService {
    Profile getProfile(Long id);

    Profile createProfile(Profile profile);

    void updateProfile(Profile profile);

    void updateProfilePassword(Long id, CurrentAndNewPassword currentAndNewPassword);

    void deleteProfile(Long id);
}
