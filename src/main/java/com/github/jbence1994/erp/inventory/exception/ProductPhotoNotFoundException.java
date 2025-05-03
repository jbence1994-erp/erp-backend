package com.github.jbence1994.erp.inventory.exception;

public class ProductPhotoNotFoundException extends RuntimeException {
    public ProductPhotoNotFoundException(Long id) {
        super(String.format("Termék a következő azonosítóval: #%d nem rendelkezik feltöltött fényképpel", id));
    }
}
