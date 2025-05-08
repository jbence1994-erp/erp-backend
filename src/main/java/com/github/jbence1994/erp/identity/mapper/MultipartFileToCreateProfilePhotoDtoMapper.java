package com.github.jbence1994.erp.identity.mapper;

import com.github.jbence1994.erp.common.mapper.MultipartFileToCreatePhotoDtoMapper;
import com.github.jbence1994.erp.identity.dto.CreateProfilePhotoDto;
import com.github.jbence1994.erp.identity.exception.ProfilePhotoUploadException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class MultipartFileToCreateProfilePhotoDtoMapper implements MultipartFileToCreatePhotoDtoMapper<CreateProfilePhotoDto> {

    @Override
    public CreateProfilePhotoDto toDto(Long id, MultipartFile file) {
        try {
            return new CreateProfilePhotoDto(
                    id,
                    file.isEmpty(),
                    file.getOriginalFilename(),
                    file.getSize(),
                    file.getContentType(),
                    file.getBytes()
            );
        } catch (IOException exception) {
            throw new ProfilePhotoUploadException(id);
        }
    }
}
