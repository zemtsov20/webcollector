package com.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "product_data")
public class ProductData {
    @Id
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "category_url")
    private String categoryUrl;

    @Column
    private String name;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "brand")
    private String brand;

    public ProductData(Long productId, String categoryUrl) {
        this.productId = productId;
        this.categoryUrl = categoryUrl;
    }

    public void addInfo(ProductData productData) {
        this.name = productData.name;
        this.brandId = productData.brandId;
        this.brand = productData.brand;
        this.supplierId = productData.supplierId;
    }
}
