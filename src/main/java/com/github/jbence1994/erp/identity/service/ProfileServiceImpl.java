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
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final PasswordManager passwordManager;

    @Override
    public Profile getProfile(Long id) {
        var profile = profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(id));

        if (profile.isDeleted()) {
            throw new ProfileNotFoundException(id);
        }

        return profile;
    }

    @Override
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

    @Override
    public void updateProfile(Profile profile) {
        profileRepository.save(profile);
    }

    @Override
    public void deleteProfile(Long id) {
        var profileToDelete = getProfile(id);

        profileToDelete.setDeleted(true);

        profileRepository.save(profileToDelete);
    }
}
