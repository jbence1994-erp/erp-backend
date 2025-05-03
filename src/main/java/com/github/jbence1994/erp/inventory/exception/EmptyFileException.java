package com.github.jbence1994.erp.inventory.exception;

public class EmptyFileException extends RuntimeException {
    public EmptyFileException(String fileName) {
        super(String.format("Üres fájl: %s", fileName));
    }
}
