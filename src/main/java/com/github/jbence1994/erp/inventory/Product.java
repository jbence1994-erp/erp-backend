package com.github.jbence1994.erp.inventory;

import java.math.BigDecimal;

public class Product {
    private Long id;
    private String name;
    private String serialNumber;
    private BigDecimal price;
    private String unit;
    private String description;
    private Supplier supplier;
    private Integer onStock;
    private String photoFileName;

    public boolean hasPhoto() {
        return photoFileName != null;
    }
}
