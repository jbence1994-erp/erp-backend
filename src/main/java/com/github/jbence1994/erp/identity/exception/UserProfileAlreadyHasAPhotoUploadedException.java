package com.github.jbence1994.erp.identity.exception;

public class UserProfileAlreadyHasAPhotoUploadedException extends RuntimeException {
    public UserProfileAlreadyHasAPhotoUploadedException(Long id) {
        super(String.format("Felhasználói fiók a következő azonosítóval: #%d már rendelkezik feltöltött fényképpel", id));
    }
}
