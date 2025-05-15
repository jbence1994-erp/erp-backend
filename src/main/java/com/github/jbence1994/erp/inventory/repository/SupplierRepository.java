package com.github.jbence1994.erp.inventory.repository;

import com.github.jbence1994.erp.inventory.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
