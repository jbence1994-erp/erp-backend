package com.github.jbence1994.erp.identity.exception;

public class UserProfileDoesNotHaveAPhotoUploadedYetException extends RuntimeException {
    public UserProfileDoesNotHaveAPhotoUploadedYetException(Long id) {
        super(String.format("Felhasználói fiók a következő azonosítóval: #%d még nem rendelkezik feltöltött fényképpel", id));
    }
}
