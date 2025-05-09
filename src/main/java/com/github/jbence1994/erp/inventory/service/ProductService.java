package com.github.jbence1994.erp.inventory.service;

import com.github.jbence1994.erp.inventory.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getProducts();

    Product getProduct(Long id);

    Product createProduct(Product product);

    void updateProduct(Product product);
}
