package com.github.jbence1994.erp.common.service;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import com.github.jbence1994.erp.common.dto.PhotoDto;
import com.github.jbence1994.erp.common.model.PhotoEntity;
import com.github.jbence1994.erp.common.util.FileUtils;
import com.github.jbence1994.erp.common.validation.FileValidator;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class PhotoService<CreateDto extends CreatePhotoDto, Dto extends PhotoDto, Entity extends PhotoEntity> {
    protected final FileUtils fileUtils;
    protected final FileValidator fileValidator;

    protected String photoUploadDirectoryPath;

    protected abstract Entity getEntity(Long id);

    protected abstract void updateEntity(Entity entity);

    protected abstract String createFileName(CreateDto createDto);

    protected abstract Dto toDto(Long id, byte[] photoBytes, String extension);

    protected abstract RuntimeException alreadyHasPhotoUploadedException(Long id);

    protected abstract RuntimeException photoUploadException(Long id);

    protected abstract RuntimeException photoDownloadException(Long id);

    protected abstract RuntimeException photoNotFoundException(Long id);

    public String uploadPhoto(CreatePhotoDto photoDto) {
        var createDto = (CreateDto) photoDto;
        var entityId = createDto.getEntityId();

        try {
            fileValidator.validate(createDto);

            var entity = getEntity(entityId);

            if (entity.hasPhoto()) {
                throw alreadyHasPhotoUploadedException(entityId);
            }

            String fileName = createFileName(createDto);
            fileUtils.store(
                    photoUploadDirectoryPath,
                    fileName,
                    createDto.getInputStream()
            );

            entity.setPhotoFileName(fileName);
            updateEntity(entity);

            return fileName;
        } catch (IOException e) {
            throw photoUploadException(entityId);
        }
    }

    public Dto getPhoto(Long id) {
        try {
            var entity = getEntity(id);

            if (!entity.hasPhoto()) {
                throw photoNotFoundException(id);
            }

            byte[] photoBytes = fileUtils.read(
                    photoUploadDirectoryPath,
                    entity.getPhotoFileName()
            );

            return toDto(id, photoBytes, entity.getPhotoFileExtension());
        } catch (IOException e) {
            throw photoDownloadException(id);
        }
    }
}
