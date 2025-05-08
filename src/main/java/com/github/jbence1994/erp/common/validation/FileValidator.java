package com.github.jbence1994.erp.common.validation;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;

public interface FileValidator {
    void validate(CreatePhotoDto photo);
}
