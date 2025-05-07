package com.github.jbence1994.erp.identity.exception;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(Long id) {
        super(String.format("Profil a következő azonosítóval: #%d nem található", id));
    }
}
