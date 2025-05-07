package com.github.jbence1994.erp.identity.exception;

public class ProfileAlreadyHasPhotoUploadedException extends RuntimeException {
    public ProfileAlreadyHasPhotoUploadedException(Long id) {
        super(String.format("Profil a következő azonosítóval: #%d már rendelkezik feltöltött fényképpel", id));
    }
}
