package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.identity.exception.ProfileNotFoundException;
import com.github.jbence1994.erp.identity.model.Profile;
import com.github.jbence1994.erp.identity.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    public Profile getProfile(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(id));
    }

    public void updateProfile(Profile profile) {
        profileRepository.save(profile);
    }
}
