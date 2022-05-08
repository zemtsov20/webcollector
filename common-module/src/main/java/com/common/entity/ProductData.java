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

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "feedback_count")
    private Integer feedbackCount;

    @Column
    private Integer rating;

}
