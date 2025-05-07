package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import com.github.jbence1994.erp.common.dto.PhotoDto;
import com.github.jbence1994.erp.common.service.PhotoService;
import com.github.jbence1994.erp.common.util.FileUtils;
import com.github.jbence1994.erp.identity.dto.CreateProfilePhotoDto;
import com.github.jbence1994.erp.identity.dto.ProfilePhotoDto;
import com.github.jbence1994.erp.identity.exception.ProfileAlreadyHasPhotoUploadedException;
import com.github.jbence1994.erp.identity.exception.ProfilePhotoNotFoundException;
import com.github.jbence1994.erp.identity.exception.ProfilePhotoUploadException;
import com.github.jbence1994.erp.inventory.validation.FileValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.github.jbence1994.erp.identity.constant.FileConstants.PROFILES_SUBDIRECTORY_NAME;

@Service
@AllArgsConstructor
public class ProfilePhotoService implements PhotoService {
    private final ProfileService profileService;
    private final FileUtils fileUtils;
    private final FileValidator fileValidator;

    @Override
    public PhotoDto getPhoto(Long id) {
        var profile = profileService.getProfile(id);

        byte[] profilePhotoBytes;

        try {
            profilePhotoBytes = fileUtils.readAllBytes(PROFILES_SUBDIRECTORY_NAME, profile.getPhotoFileName());
        } catch (Exception exception) {
            throw new ProfilePhotoNotFoundException(id);
        }

        return new ProfilePhotoDto(id, profilePhotoBytes, profile.getPhotoFileExtension());
    }

    @Override
    public String uploadPhoto(CreatePhotoDto photo) {
        var profilePhoto = (CreateProfilePhotoDto) photo;

        try {
            fileValidator.validate(profilePhoto);

            var profile = profileService.getProfile(profilePhoto.getProfileId());

            if (profile.hasPhoto()) {
                throw new ProfileAlreadyHasPhotoUploadedException(profilePhoto.getProfileId());
            }

            var directoryStructurePath = fileUtils.createPhotoUploadsDirectoryStructure(PROFILES_SUBDIRECTORY_NAME);
            profile.setPhotoFileName(fileUtils.storePhoto(profilePhoto, directoryStructurePath));
            profileService.updateProfile(profile);

            return profile.getPhotoFileName();
        } catch (IOException exception) {
            throw new ProfilePhotoUploadException(profilePhoto.getProfileId());
        }
    }
}
