package com.github.jbence1994.erp.identity.exception;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(Long id) {
        super(String.format("Felhasználói fiók a következő azonosítóval: #%d nem található", id));
    }
}
