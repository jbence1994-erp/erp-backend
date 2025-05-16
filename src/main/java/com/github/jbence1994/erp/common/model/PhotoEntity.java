package com.github.jbence1994.erp.common.model;

public interface PhotoEntity {
    boolean hasPhoto();

    String getPhotoFileName();

    void setPhotoFileName(String fileName);

    String getPhotoFileExtension();
}
