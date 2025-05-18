package com.github.jbence1994.erp.common.model;

import org.springframework.util.StringUtils;

public abstract class PhotoEntity {
    public abstract String getPhotoFileName();

    public abstract void setPhotoFileName(String fileName);

    public boolean hasPhoto() {
        return getPhotoFileName() != null;
    }

    public String getPhotoFileExtension() {
        return StringUtils.getFilenameExtension(getPhotoFileName());
    }
}
