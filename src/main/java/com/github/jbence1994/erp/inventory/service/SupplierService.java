package com.github.jbence1994.erp.inventory.service;

import com.github.jbence1994.erp.inventory.model.Supplier;

public interface SupplierService {
    Supplier createSupplier(Supplier supplier);

    void updateSupplier(Supplier supplier);
}
