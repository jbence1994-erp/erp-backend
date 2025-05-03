package com.github.jbence1994.erp.common.service;

import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import com.github.jbence1994.erp.common.dto.PhotoDto;

public interface PhotoService {
    PhotoDto getPhoto(Long id);

    String uploadPhoto(CreatePhotoDto photo);
}
