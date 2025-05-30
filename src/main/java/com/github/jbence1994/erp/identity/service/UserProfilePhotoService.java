package com.github.jbence1994.erp.identity.service;

import com.github.jbence1994.erp.common.service.PhotoService;
import com.github.jbence1994.erp.common.util.FileUtils;
import com.github.jbence1994.erp.common.validation.FileValidator;
import com.github.jbence1994.erp.identity.dto.CreateUserProfilePhotoDto;
import com.github.jbence1994.erp.identity.dto.UserProfilePhotoDto;
import com.github.jbence1994.erp.identity.exception.UserProfileAlreadyHasAPhotoUploadedException;
import com.github.jbence1994.erp.identity.exception.UserProfileDoesNotHaveAPhotoUploadedYetException;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoDeleteException;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoDownloadException;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoNotFoundException;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoUploadException;
import com.github.jbence1994.erp.identity.model.UserProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserProfilePhotoService extends PhotoService<CreateUserProfilePhotoDto, UserProfilePhotoDto, UserProfile> {
    private final UserProfileService userProfileService;

    public UserProfilePhotoService(
            @Value("${erp.photo-upload-directory-path.user-profiles}") final String photoUploadDirectoryPath,
            final FileUtils fileUtils,
            final UserProfileService userProfileService,
            final FileValidator fileValidator
    ) {
        super(fileUtils, fileValidator);
        this.userProfileService = userProfileService;
        this.photoUploadDirectoryPath = photoUploadDirectoryPath;
    }

    @Override
    protected UserProfile getEntity(Long id) {
        return userProfileService.getUserProfile(id);
    }

    @Override
    protected void updateEntity(UserProfile userProfile) {
        userProfileService.updateUserProfile(userProfile);
    }

    @Override
    protected String createFileName(CreateUserProfilePhotoDto createUserProfilePhotoDto) {
        return createUserProfilePhotoDto.createFileName();
    }

    @Override
    protected UserProfilePhotoDto dto(Long id, byte[] photoBytes, String extension) {
        return new UserProfilePhotoDto(id, photoBytes, extension);
    }

    @Override
    protected RuntimeException alreadyHasAPhotoUploadedException(Long id) {
        return new UserProfileAlreadyHasAPhotoUploadedException(id);
    }

    @Override
    protected RuntimeException doesNotHaveAPhotoUploadedYetException(Long id) {
        return new UserProfileDoesNotHaveAPhotoUploadedYetException(id);
    }

    @Override
    protected RuntimeException photoUploadException(Long id) {
        return new UserProfilePhotoUploadException(id);
    }

    @Override
    protected RuntimeException photoDownloadException(Long id) {
        return new UserProfilePhotoDownloadException(id);
    }

    @Override
    protected RuntimeException photoDeleteException(Long id) {
        return new UserProfilePhotoDeleteException(id);
    }

    @Override
    protected RuntimeException photoNotFoundException(Long id) {
        return new UserProfilePhotoNotFoundException(id);
    }
}
