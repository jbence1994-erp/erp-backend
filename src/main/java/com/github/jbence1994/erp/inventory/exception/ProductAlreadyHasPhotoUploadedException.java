package com.github.jbence1994.erp.inventory.exception;

public class ProductAlreadyHasPhotoUploadedException extends RuntimeException {
    public ProductAlreadyHasPhotoUploadedException(Long id) {
        super(String.format("Termék a következő azonosítóval: #%d már rendelkezik feltöltött fényképpel", id));
    }
}
