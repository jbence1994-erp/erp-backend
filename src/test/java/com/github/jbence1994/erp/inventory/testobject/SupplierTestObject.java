package com.github.jbence1994.erp.inventory.testobject;

import com.github.jbence1994.erp.inventory.model.Supplier;

import static com.github.jbence1994.erp.inventory.constant.SupplierTestConstants.SUPPLIER_1_EMAIL;
import static com.github.jbence1994.erp.inventory.constant.SupplierTestConstants.SUPPLIER_1_NAME;
import static com.github.jbence1994.erp.inventory.constant.SupplierTestConstants.SUPPLIER_1_PHONE;

public final class SupplierTestObject {
    public static Supplier supplier1() {
        return new Supplier(
                1L,
                SUPPLIER_1_NAME,
                SUPPLIER_1_PHONE,
                SUPPLIER_1_EMAIL
        );
    }
}
