package com.github.jbence1994.erp.common.service;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import com.github.jbence1994.erp.common.dto.PhotoDto;

public interface PhotoService {
    String UPLOADS_DIRECTORY_NAME = "uploads";
    String PHOTOS_SUBDIRECTORY_NAME = "photos";

    String uploadPhoto(CreatePhotoDto photo);

    PhotoDto getPhoto(Long id);
}
