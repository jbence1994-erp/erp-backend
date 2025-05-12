package com.github.jbence1994.erp.identity.exception;

public class UserProfilePhotoUploadException extends RuntimeException {
    public UserProfilePhotoUploadException(Long id) {
        super(String.format("Fénykép feltöltése az alábbi felhasználói fiókhoz: #%d sikeretelen volt", id));
    }
}
