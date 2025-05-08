package com.github.jbence1994.erp.inventory.service;

import com.github.jbence1994.erp.inventory.model.Product;

public interface ProductService {
    Product getProduct(Long id);

    void updateProduct(Product product);
}
