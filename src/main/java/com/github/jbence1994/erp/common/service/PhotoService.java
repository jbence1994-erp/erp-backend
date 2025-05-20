package com.github.jbence1994.erp.common.service;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import com.github.jbence1994.erp.common.dto.PhotoDto;
import com.github.jbence1994.erp.common.model.PhotoEntity;
import com.github.jbence1994.erp.common.util.FileUtils;
import com.github.jbence1994.erp.common.validation.FileValidator;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class PhotoService<C extends CreatePhotoDto, D extends PhotoDto, E extends PhotoEntity> {
    protected final FileUtils fileUtils;
    protected final FileValidator fileValidator;

    protected String photoUploadDirectoryPath;

    protected abstract E getEntity(Long id);

    protected abstract void updateEntity(E entity);

    protected abstract String createFileName(C createPhotoDto);

    protected abstract D dto(Long id, byte[] photoBytes, String extension);

    protected abstract RuntimeException alreadyHasPhotoUploadedException(Long id);

    protected abstract RuntimeException photoUploadException(Long id);

    protected abstract RuntimeException photoDownloadException(Long id);

    protected abstract RuntimeException photoDeleteException(Long id);

    protected abstract RuntimeException photoNotFoundException(Long id);

    public String uploadPhoto(CreatePhotoDto photoDto) {
        var createPhotoDto = (C) photoDto;
        var entityId = createPhotoDto.getEntityId();

        try {
            fileValidator.validate(createPhotoDto);

            var entity = getEntity(entityId);

            if (entity.hasPhoto()) {
                throw alreadyHasPhotoUploadedException(entityId);
            }

            String fileName = createFileName(createPhotoDto);
            fileUtils.store(
                    photoUploadDirectoryPath,
                    fileName,
                    createPhotoDto.getInputStream()
            );

            entity.setPhotoFileName(fileName);
            updateEntity(entity);

            return fileName;
        } catch (IOException exception) {
            throw photoUploadException(entityId);
        }
    }

    public D getPhoto(Long id) {
        try {
            var entity = getEntity(id);

            if (!entity.hasPhoto()) {
                throw photoNotFoundException(id);
            }

            byte[] photoBytes = fileUtils.read(
                    photoUploadDirectoryPath,
                    entity.getPhotoFileName()
            );

            return dto(id, photoBytes, entity.getPhotoFileExtension());
        } catch (IOException exception) {
            throw photoDownloadException(id);
        }
    }

    public void deletePhoto(Long id) {
        try {
            var entity = getEntity(id);

            fileUtils.delete(
                    photoUploadDirectoryPath,
                    entity.getPhotoFileName()
            );

            entity.setPhotoFileName(null);
            updateEntity(entity);
        } catch (IOException exception) {
            throw photoDeleteException(id);
        }
    }
}
