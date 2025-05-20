package com.github.jbence1994.erp.inventory.exception;

public class ProductDoesNotHaveAPhotoUploadedYetException extends RuntimeException {
    public ProductDoesNotHaveAPhotoUploadedYetException(Long id) {
        super(String.format("Termék a következő azonosítóval: #%d még nem rendelkezik feltöltött fényképpel", id));
    }
}
