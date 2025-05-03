package com.github.jbence1994.erp.inventory.exception;

public class InvalidFileExtensionException extends RuntimeException {
    public InvalidFileExtensionException(String extension) {
        super(String.format("Hibás fájlformátum: .%s", extension));
    }
}
