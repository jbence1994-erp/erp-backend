package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.identity.exception.ProfileAlreadyExistException;
import com.github.jbence1994.erp.identity.exception.ProfileNotFoundException;
import com.github.jbence1994.erp.identity.model.Profile;
import com.github.jbence1994.erp.identity.repository.ProfileRepository;
import com.github.jbence1994.erp.identity.util.PasswordManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final PasswordManager passwordManager;

    public Profile getProfile(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(id));
    }

    public Profile createProfile(Profile profile) {
        var userId = profile.getUser().getId();

        if (isProfileAlreadyExists(userId)) {
            throw new ProfileAlreadyExistException(userId);
        }

        profile.setPassword(passwordManager.encode(profile.getPassword()));

        return profileRepository.save(profile);
    }

    private boolean isProfileAlreadyExists(Long userId) {
        return profileRepository.findByUserId(userId).isPresent();
    }

    public void updateProfile(Profile profile) {
        profileRepository.save(profile);
    }
}
