package com.github.jbence1994.erp.inventory.constant;

import java.util.Arrays;
import java.util.List;

public interface PhotoTestConstants {
    Integer FILE_SIZE = 10_485_760;
    String JPG = "jpg";
    String JPEG = "jpeg";
    String PNG = "png";
    String BMP = "bmp";
    String TXT = "txt";
    List<String> ALLOWED_FILE_EXTENSIONS = Arrays.asList(JPG, JPEG, PNG, BMP);
    String CONTENT_TYPE_IMAGE_JPEG = String.format("image/%s", JPEG);
    String IMAGE = "image";
    String FILE_NAME = "file_name";
    String FILE_NAME_JPG = String.format("%s.%s", FILE_NAME, JPG);
    String FILE_NAME_JPEG = String.format("%s.%s", FILE_NAME, JPEG);
    String FILE_NAME_PNG = String.format("%s.%s", FILE_NAME, PNG);
    String FILE_NAME_BMP = String.format("%s.%s", FILE_NAME, BMP);
    String INVALID_FILE_NAME = "file_name.txt";
    String TEXT_PLAIN = "text/plain";
    String PHOTO_FILE_NAME = "7a759fbb-39d8-4b3b-af57-4266980901dc.jpeg";
    String UPLOADS_DIRECTORY_WITH_PHOTOS_SUBDIRECTORY_AND_CUSTOM_SUBDIRECTORY = "uploads/photos/products/";
}
