package com.github.jbence1994.erp.inventory.repository;

import com.github.jbence1994.erp.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
