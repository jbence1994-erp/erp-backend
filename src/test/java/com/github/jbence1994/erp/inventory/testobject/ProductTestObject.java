package com.github.jbence1994.erp.inventory.testobject;

import com.github.jbence1994.erp.inventory.model.Product;

import java.math.BigDecimal;

import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.PHOTO_FILE_NAME;
import static com.github.jbence1994.erp.inventory.constant.ProductTestConstants.PRODUCT_1_DESCRIPTION;
import static com.github.jbence1994.erp.inventory.constant.ProductTestConstants.PRODUCT_1_NAME;
import static com.github.jbence1994.erp.inventory.constant.ProductTestConstants.PRODUCT_1_SERIAL_NUMBER;
import static com.github.jbence1994.erp.inventory.constant.ProductTestConstants.PRODUCT_1_UNIT;
import static com.github.jbence1994.erp.inventory.constant.ProductTestConstants.PRODUCT_2_DESCRIPTION;
import static com.github.jbence1994.erp.inventory.constant.ProductTestConstants.PRODUCT_2_NAME;
import static com.github.jbence1994.erp.inventory.constant.ProductTestConstants.PRODUCT_2_SERIAL_NUMBER;
import static com.github.jbence1994.erp.inventory.constant.ProductTestConstants.PRODUCT_2_UNIT;
import static com.github.jbence1994.erp.inventory.testobject.SupplierTestObject.supplier1;

public final class ProductTestObject {
    public static Product product1() {
        return new Product(
                1L,
                PRODUCT_1_NAME,
                PRODUCT_1_SERIAL_NUMBER,
                BigDecimal.valueOf(10000.00),
                PRODUCT_1_UNIT,
                PRODUCT_1_DESCRIPTION,
                supplier1(),
                10,
                null
        );
    }

    public static Product product1WithPhoto() {
        return new Product(
                1L,
                PRODUCT_1_NAME,
                PRODUCT_1_SERIAL_NUMBER,
                BigDecimal.valueOf(10000.00),
                PRODUCT_1_UNIT,
                PRODUCT_1_DESCRIPTION,
                supplier1(),
                10,
                PHOTO_FILE_NAME
        );
    }

    public static Product product2() {
        return new Product(
                2L,
                PRODUCT_2_NAME,
                PRODUCT_2_SERIAL_NUMBER,
                BigDecimal.valueOf(20000.00),
                PRODUCT_2_UNIT,
                PRODUCT_2_DESCRIPTION,
                supplier1(),
                20,
                null
        );
    }
}
