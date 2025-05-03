package com.github.jbence1994.erp.inventory.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super(String.format("Termék a következő azonosítóval: #%d nem található", id));
    }
}
