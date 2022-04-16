package com.common.entity;

import com.common.enums.State;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @Enumerated
    private State state;

    @OneToMany(mappedBy = "productData", cascade = CascadeType.ALL)
    private List<ProductDataTs> productDataTsList;

    @Lob
    private String json;

    public ProductData(Long productId, String categoryUrl) {
        this.productId = productId;
        this.categoryUrl = categoryUrl;
        this.state = State.QUEUED;
    }

    public void addInfo(ProductData productData) {
        this.name = productData.name;
        this.brandId = productData.brandId;
        this.brand = productData.brand;
        this.supplierId = productData.supplierId;
    }
}
