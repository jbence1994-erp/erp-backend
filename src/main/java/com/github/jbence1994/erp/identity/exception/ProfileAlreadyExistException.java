package com.github.jbence1994.erp.identity.exception;

public class ProfileAlreadyExistException extends RuntimeException {
    public ProfileAlreadyExistException(Long userId) {
        super(String.format("Felhasználói fiók a következő azonosítóval: #%d már létezik.", userId));
    }
}
