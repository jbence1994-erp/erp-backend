package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.identity.dto.UserProfileCurrentAndNewPassword;
import com.github.jbence1994.erp.identity.exception.UserProfileCurrentPasswordAndPasswordNotMatchingException;
import com.github.jbence1994.erp.identity.exception.UserProfileNotFoundException;
import com.github.jbence1994.erp.identity.model.UserProfile;
import com.github.jbence1994.erp.identity.repository.UserProfileRepository;
import com.github.jbence1994.erp.identity.util.PasswordManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final PasswordManager passwordManager;

    @Override
    public UserProfile getUserProfile(Long id) {
        return userProfileRepository.findById(id)
                .filter(userProfile -> !userProfile.isDeleted())
                .orElseThrow(() -> new UserProfileNotFoundException(id));
    }

    private UserProfile getDeletedUserProfile(Long id) {
        return userProfileRepository.findById(id)
                .orElseThrow(() -> new UserProfileNotFoundException(id));
    }

    @Override
    public UserProfile createUserProfile(UserProfile userProfile) {
        userProfile.updatePassword(passwordManager.encode(userProfile.getPassword()));

        return userProfileRepository.save(userProfile);
    }

    @Override
    public void updateUserProfile(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    @Override
    public void updateUserProfilePassword(Long id, UserProfileCurrentAndNewPassword userProfileCurrentAndNewPassword) {
        var userProfile = getUserProfile(id);

        var userProfileCurrentPassword = userProfileCurrentAndNewPassword.currentPassword();
        var userProfileNewPassword = userProfileCurrentAndNewPassword.newPassword();

        if (!passwordManager.verify(userProfileCurrentPassword, userProfile.getPassword())) {
            throw new UserProfileCurrentPasswordAndPasswordNotMatchingException();
        }

        userProfile.updatePassword(passwordManager.encode(userProfileNewPassword));

        userProfileRepository.save(userProfile);
    }

    @Override
    public void deleteUserProfile(Long id) {
        var userProfile = getUserProfile(id);

        userProfile.delete();

        userProfileRepository.save(userProfile);
    }

    @Override
    public void restoreUserProfile(Long id) {
        var userProfile = getDeletedUserProfile(id);

        userProfile.restore();

        userProfileRepository.save(userProfile);
    }
}
