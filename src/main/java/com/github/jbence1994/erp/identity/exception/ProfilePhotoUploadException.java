package com.github.jbence1994.erp.identity.exception;

public class ProfilePhotoUploadException extends RuntimeException {
    public ProfilePhotoUploadException(Long id) {
        super(String.format("Fénykép feltöltése az alábbi felhasználói fiókhoz: #%d sikeretelen volt", id));
    }
}
