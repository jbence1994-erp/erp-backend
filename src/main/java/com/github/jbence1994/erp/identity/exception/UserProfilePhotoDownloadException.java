package com.github.jbence1994.erp.identity.exception;

public class UserProfilePhotoDownloadException extends RuntimeException {
    public UserProfilePhotoDownloadException(Long id) {
        super(String.format("Fénykép letöltése az alábbi felhasználói fiókhoz: #%d sikertelen volt", id));
    }
}
