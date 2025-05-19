package com.github.jbence1994.erp.identity.exception;

public class UserProfilePhotoDeleteException extends RuntimeException {
    public UserProfilePhotoDeleteException(Long id) {
        super(String.format("Fénykép törlése az alábbi felhasználói fióknál: #%d sikeretelen volt", id));
    }
}
