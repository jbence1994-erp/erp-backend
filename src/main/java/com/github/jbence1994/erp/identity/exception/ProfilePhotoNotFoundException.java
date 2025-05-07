package com.github.jbence1994.erp.identity.exception;

public class ProfilePhotoNotFoundException extends RuntimeException {
    public ProfilePhotoNotFoundException(Long id) {
        super(String.format("Profil a következő azonosítóval: #%d nem rendelkezik feltöltött fényképpel", id));
    }
}
