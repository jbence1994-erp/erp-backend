package com.github.jbence1994.erp.identity.exception;

public class CurrentPasswordAndPasswordNotMatchingException extends RuntimeException {
    public CurrentPasswordAndPasswordNotMatchingException() {
        super("A jelenlegi jelszó hibás.");
    }
}
