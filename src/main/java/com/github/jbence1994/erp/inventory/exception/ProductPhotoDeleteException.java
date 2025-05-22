package com.github.jbence1994.erp.inventory.exception;

public class ProductPhotoDeleteException extends RuntimeException {
    public ProductPhotoDeleteException(Long id) {
        super(String.format("Fénykép törlése az alábbi terméknél: #%d sikertelen volt", id));
    }
}
