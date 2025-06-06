package com.github.jbence1994.erp.identity.exception;

public class UserProfileNotFoundException extends RuntimeException {
    public UserProfileNotFoundException(Long id) {
        super(String.format("Felhasználói fiók a következő azonosítóval: #%d nem található", id));
    }
}
