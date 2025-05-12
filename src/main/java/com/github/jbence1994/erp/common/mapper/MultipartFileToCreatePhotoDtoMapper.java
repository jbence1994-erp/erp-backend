package com.github.jbence1994.erp.common.mapper;

import org.springframework.web.multipart.MultipartFile;

public interface MultipartFileToCreatePhotoDtoMapper<Dto> {
    Dto toDto(Long id, MultipartFile file);
}
