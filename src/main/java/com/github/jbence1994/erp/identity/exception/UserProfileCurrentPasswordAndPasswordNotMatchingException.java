package com.github.jbence1994.erp.identity.exception;

public class UserProfileCurrentPasswordAndPasswordNotMatchingException extends RuntimeException {
    public UserProfileCurrentPasswordAndPasswordNotMatchingException() {
        super("A jelenlegi jelszó hibás.");
    }
}
