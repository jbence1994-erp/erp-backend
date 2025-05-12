package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import com.github.jbence1994.erp.common.dto.PhotoDto;
import com.github.jbence1994.erp.common.service.PhotoService;
import com.github.jbence1994.erp.common.util.FileUtils;
import com.github.jbence1994.erp.common.validation.FileValidator;
import com.github.jbence1994.erp.identity.dto.CreateProfilePhotoDto;
import com.github.jbence1994.erp.identity.dto.ProfilePhotoDto;
import com.github.jbence1994.erp.identity.exception.ProfileAlreadyHasPhotoUploadedException;
import com.github.jbence1994.erp.identity.exception.ProfilePhotoNotFoundException;
import com.github.jbence1994.erp.identity.exception.ProfilePhotoUploadException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class ProfilePhotoService implements PhotoService {
    private final ProfileService profileService;
    private final FileUtils fileUtils;
    private final FileValidator fileValidator;

    private static final String PROFILES_SUBDIRECTORY_NAME = "profiles";

    @Override
    public String uploadPhoto(CreatePhotoDto photo) {
        var profilePhoto = (CreateProfilePhotoDto) photo;

        try {
            fileValidator.validate(profilePhoto);

            var profile = profileService.getProfile(profilePhoto.getProfileId());

            if (profile.hasPhoto()) {
                throw new ProfileAlreadyHasPhotoUploadedException(profilePhoto.getProfileId());
            }

            var photoName = profilePhoto.createFileName();
            fileUtils.store(
                    getPhotoUploadsPath(),
                    photoName,
                    profilePhoto.getInputStream()
            );
            profile.setPhotoFileName(photoName);
            profileService.updateProfile(profile);

            return profile.getPhotoFileName();
        } catch (IOException exception) {
            throw new ProfilePhotoUploadException(profilePhoto.getProfileId());
        }
    }

    @Override
    public PhotoDto getPhoto(Long id) {
        try {
            var profile = profileService.getProfile(id);

            byte[] photoBytes = fileUtils.read(
                    getPhotoUploadsPath(),
                    profile.getPhotoFileName()
            );

            return new ProfilePhotoDto(id, photoBytes, profile.getPhotoFileExtension());
        } catch (Exception exception) {
            throw new ProfilePhotoNotFoundException(id);
        }
    }

    // FIXME: Move to environment variable and YAML config
    private String getPhotoUploadsPath() {
        return String.format(
                "%s/%s/%s",
                UPLOADS_DIRECTORY_NAME,
                PHOTOS_SUBDIRECTORY_NAME,
                PROFILES_SUBDIRECTORY_NAME
        );
    }
}
