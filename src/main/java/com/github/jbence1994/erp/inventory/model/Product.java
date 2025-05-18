package com.github.jbence1994.erp.inventory.model;

import com.github.jbence1994.erp.common.model.PhotoEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product extends PhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String serialNumber;

    private BigDecimal price;

    private String unit;

    private String description;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    private Integer onStock;

    private String photoFileName;

    @Override
    public String getPhotoFileName() {
        return photoFileName;
    }

    @Override
    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }
}
