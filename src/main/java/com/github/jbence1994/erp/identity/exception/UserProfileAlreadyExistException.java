package com.github.jbence1994.erp.identity.exception;

public class UserProfileAlreadyExistException extends RuntimeException {
    public UserProfileAlreadyExistException(Long userId) {
        super(String.format("Felhasználói fiók a következő azonosítóval: #%d már létezik.", userId));
    }
}
