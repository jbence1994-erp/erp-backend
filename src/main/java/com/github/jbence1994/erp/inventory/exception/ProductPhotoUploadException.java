package com.github.jbence1994.erp.inventory.exception;

public class ProductPhotoUploadException extends RuntimeException {
    public ProductPhotoUploadException(Long id) {
        super(String.format("Fénykép feltöltése az alábbi termékhez: #%d sikeretelen volt", id));
    }
}
