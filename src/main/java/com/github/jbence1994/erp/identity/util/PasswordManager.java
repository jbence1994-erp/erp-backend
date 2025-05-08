package com.github.jbence1994.erp.identity.util;

public interface PasswordManager {
    String encode(String rawPassword);

    boolean verify(String rawPassword, String hashedPassword);
}
