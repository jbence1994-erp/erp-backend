package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import com.github.jbence1994.erp.common.dto.PhotoDto;
import com.github.jbence1994.erp.common.service.PhotoService;
import com.github.jbence1994.erp.common.util.FileUtils;
import com.github.jbence1994.erp.common.validation.FileValidator;
import com.github.jbence1994.erp.identity.dto.CreateUserProfilePhotoDto;
import com.github.jbence1994.erp.identity.dto.UserProfilePhotoDto;
import com.github.jbence1994.erp.identity.exception.UserProfileAlreadyHasPhotoUploadedException;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoNotFoundException;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserProfilePhotoService implements PhotoService {
    private final UserProfileService userProfileService;
    private final FileUtils fileUtils;
    private final FileValidator fileValidator;

    @Value("${photo_upload_directory_path.user_profiles}")
    private String photoUploadDirectoryPath;

    @Override
    public String uploadPhoto(CreatePhotoDto photo) {
        var userProfilePhoto = (CreateUserProfilePhotoDto) photo;

        try {
            fileValidator.validate(userProfilePhoto);

            var profile = userProfileService.getUserProfile(userProfilePhoto.getUserProfileId());

            if (profile.hasPhoto()) {
                throw new UserProfileAlreadyHasPhotoUploadedException(userProfilePhoto.getUserProfileId());
            }

            var photoName = userProfilePhoto.createFileName();
            fileUtils.store(
                    photoUploadDirectoryPath,
                    photoName,
                    userProfilePhoto.getInputStream()
            );
            profile.setPhotoFileName(photoName);
            userProfileService.updateUserProfile(profile);

            return profile.getPhotoFileName();
        } catch (IOException exception) {
            throw new UserProfilePhotoUploadException(userProfilePhoto.getUserProfileId());
        }
    }

    @Override
    public PhotoDto getPhoto(Long id) {
        try {
            var profile = userProfileService.getUserProfile(id);

            byte[] photoBytes = fileUtils.read(
                    photoUploadDirectoryPath,
                    profile.getPhotoFileName()
            );

            return new UserProfilePhotoDto(id, photoBytes, profile.getPhotoFileExtension());
        } catch (Exception exception) {
            throw new UserProfilePhotoNotFoundException(id);
        }
    }
}
