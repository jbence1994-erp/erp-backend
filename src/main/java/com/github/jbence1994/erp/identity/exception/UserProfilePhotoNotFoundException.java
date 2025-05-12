package com.github.jbence1994.erp.identity.exception;

public class UserProfilePhotoNotFoundException extends RuntimeException {
    public UserProfilePhotoNotFoundException(Long id) {
        super(String.format("Felhasználói fiók a következő azonosítóval: #%d nem rendelkezik feltöltött fényképpel", id));
    }
}
