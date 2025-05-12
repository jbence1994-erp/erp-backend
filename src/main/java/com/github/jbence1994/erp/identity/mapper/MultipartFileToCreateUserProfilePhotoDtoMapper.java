package com.github.jbence1994.erp.identity.mapper;

import com.github.jbence1994.erp.common.mapper.MultipartFileToCreatePhotoDtoMapper;
import com.github.jbence1994.erp.identity.dto.CreateUserProfilePhotoDto;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoUploadException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class MultipartFileToCreateUserProfilePhotoDtoMapper implements MultipartFileToCreatePhotoDtoMapper<CreateUserProfilePhotoDto> {

    @Override
    public CreateUserProfilePhotoDto toDto(Long userProfileId, MultipartFile file) {
        try {
            return new CreateUserProfilePhotoDto(
                    userProfileId,
                    file.isEmpty(),
                    file.getOriginalFilename(),
                    file.getSize(),
                    file.getContentType(),
                    file.getBytes()
            );
        } catch (IOException exception) {
            throw new UserProfilePhotoUploadException(userProfileId);
        }
    }
}
