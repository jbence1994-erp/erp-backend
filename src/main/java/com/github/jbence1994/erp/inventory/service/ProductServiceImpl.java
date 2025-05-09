package com.github.jbence1994.erp.inventory.service;

import com.github.jbence1994.erp.inventory.exception.ProductNotFoundException;
import com.github.jbence1994.erp.inventory.model.Product;
import com.github.jbence1994.erp.inventory.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void updateProduct(Product product) {
        productRepository.save(product);
    }
}
