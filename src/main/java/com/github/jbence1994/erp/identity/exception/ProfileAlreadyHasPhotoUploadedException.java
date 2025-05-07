package com.github.jbence1994.erp.identity.exception;

public class ProfileAlreadyHasPhotoUploadedException extends RuntimeException {
    public ProfileAlreadyHasPhotoUploadedException(Long id) {
        super(String.format("Felhasználói fiók a következő azonosítóval: #%d már rendelkezik feltöltött fényképpel", id));
    }
}
