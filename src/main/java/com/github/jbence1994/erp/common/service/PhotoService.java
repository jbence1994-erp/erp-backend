package com.github.jbence1994.erp.common.service;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import com.github.jbence1994.erp.common.dto.PhotoDto;

public interface PhotoService {
    String uploadPhoto(CreatePhotoDto photo);

    PhotoDto getPhoto(Long id);
}
