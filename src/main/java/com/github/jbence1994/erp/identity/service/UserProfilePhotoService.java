package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import com.github.jbence1994.erp.common.dto.PhotoDto;
import com.github.jbence1994.erp.common.service.PhotoService;
import com.github.jbence1994.erp.common.util.FileUtils;
import com.github.jbence1994.erp.common.validation.FileValidator;
import com.github.jbence1994.erp.identity.dto.CreateUserProfilePhotoDto;
import com.github.jbence1994.erp.identity.dto.UserProfilePhotoDto;
import com.github.jbence1994.erp.identity.exception.UserProfileAlreadyHasPhotoUploadedException;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoDownloadException;
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

    @Value("${erp.photo-upload-directory-path.user-profiles}")
    private String photoUploadDirectoryPath;

    @Override
    public String uploadPhoto(CreatePhotoDto photo) {
        var userProfilePhoto = (CreateUserProfilePhotoDto) photo;

        try {
            fileValidator.validate(userProfilePhoto);

            var userProfile = userProfileService.getUserProfile(userProfilePhoto.getUserProfileId());

            if (userProfile.hasPhoto()) {
                throw new UserProfileAlreadyHasPhotoUploadedException(userProfilePhoto.getUserProfileId());
            }

            var photoName = userProfilePhoto.createFileName();
            fileUtils.store(
                    photoUploadDirectoryPath,
                    photoName,
                    userProfilePhoto.getInputStream()
            );
            userProfile.setPhotoFileName(photoName);
            userProfileService.updateUserProfile(userProfile);

            return userProfile.getPhotoFileName();
        } catch (IOException exception) {
            throw new UserProfilePhotoUploadException(userProfilePhoto.getUserProfileId());
        }
    }

    @Override
    public PhotoDto getPhoto(Long id) {
        try {
            var userProfile = userProfileService.getUserProfile(id);

            if (!userProfile.hasPhoto()) {
                throw new UserProfilePhotoNotFoundException(id);
            }

            var photoBytes = fileUtils.read(
                    photoUploadDirectoryPath,
                    userProfile.getPhotoFileName()
            );

            return new UserProfilePhotoDto(id, photoBytes, userProfile.getPhotoFileExtension());
        } catch (IOException exception) {
            throw new UserProfilePhotoDownloadException(id);
        }
    }
}
