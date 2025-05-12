package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.identity.dto.UserProfileCurrentAndNewPassword;
import com.github.jbence1994.erp.identity.exception.UserProfileAlreadyExistException;
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

    @Override
    public UserProfile getUserProfile(String email) {
        return userProfileRepository.findByEmail(email)
                .filter(userProfile -> !userProfile.isDeleted())
                .orElseThrow(() -> new UserProfileNotFoundException(email));
    }

    @Override
    public UserProfile createUserProfile(UserProfile userProfile) {
        var userProfileId = userProfile.getId();

        if (isUserProfileAlreadyExists(userProfile.getId())) {
            throw new UserProfileAlreadyExistException(userProfileId);
        }

        userProfile.setPassword(passwordManager.encode(userProfile.getPassword()));

        return userProfileRepository.save(userProfile);
    }

    @Override
    public void updateUserProfile(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    @Override
    public void updateUserProfilePassword(Long id, UserProfileCurrentAndNewPassword userProfileCurrentAndNewPassword) {
        var userProfileToUpdate = getUserProfile(id);

        var userProfileCurrentPassword = userProfileCurrentAndNewPassword.currentPassword();
        var userProfileNewPassword = userProfileCurrentAndNewPassword.newPassword();

        if (!passwordManager.verify(userProfileCurrentPassword, userProfileToUpdate.getPassword())) {
            throw new UserProfileCurrentPasswordAndPasswordNotMatchingException();
        }

        userProfileToUpdate.setPassword(passwordManager.encode(userProfileNewPassword));

        userProfileRepository.save(userProfileToUpdate);
    }

    @Override
    public void deleteUserProfile(Long id) {
        var userProfileToDelete = getUserProfile(id);

        userProfileToDelete.setDeleted(true);

        userProfileRepository.save(userProfileToDelete);
    }

    private boolean isUserProfileAlreadyExists(Long userId) {
        return userProfileRepository.findById(userId).isPresent();
    }
}
