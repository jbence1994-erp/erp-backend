package com.github.jbence1994.erp.inventory.validation;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import com.github.jbence1994.erp.inventory.config.FileExtensionsConfig;
import com.github.jbence1994.erp.inventory.exception.EmptyFileException;
import com.github.jbence1994.erp.inventory.exception.InvalidFileExtensionException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FileValidator {
    private final FileExtensionsConfig fileExtensionsConfig;

    public void validate(CreatePhotoDto photo) {
        if (photo.isEmpty()) {
            throw new EmptyFileException(photo.getOriginalFilename());
        }

        var fileExtension = photo.getFileExtension();

        if (!isValid(fileExtension)) {
            throw new InvalidFileExtensionException(fileExtension);
        }
    }

    private boolean isValid(String fileExtension) {
        return fileExtensionsConfig.getAllowedFileExtensions()
                .stream()
                .anyMatch(extension -> extension.equals(fileExtension));

    }
}
