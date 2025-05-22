package com.github.jbence1994.erp.inventory.exception;

public class ProductPhotoDownloadException extends RuntimeException {
    public ProductPhotoDownloadException(Long id) {
        super(String.format("Fénykép letöltése az alábbi termékhez: #%d sikertelen volt", id));
    }
}
