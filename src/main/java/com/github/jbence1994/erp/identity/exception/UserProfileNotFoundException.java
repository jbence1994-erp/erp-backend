package com.github.jbence1994.erp.identity.exception;

public class UserProfileNotFoundException extends RuntimeException {
    public UserProfileNotFoundException(Long id) {
        super(String.format("Felhasználói fiók a következő azonosítóval: #%d nem található", id));
    }

    public UserProfileNotFoundException(String email) {
        super(String.format("Felhasználói fiók a következő e-mail címmel: #%s nem található", email));
    }
}
