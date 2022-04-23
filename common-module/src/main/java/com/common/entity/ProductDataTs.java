package com.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@IdClass(ProductDataTsId.class)
@Table(name = "product_data_ts")
public class ProductDataTs {
    @Id
    private Long productId;

    @Id
    @Column(name = "taken_in", unique=true, nullable=false)
    private Date takenIn;

    @Column
    private Integer price;

    @Column
    private Integer priceWithSale;

    @Column
    private Integer quantity;

//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "product_id", nullable = false)
//    private ProductData productData;

    public ProductDataTs(Date takenIn, Integer price, Integer priceWithSale, Integer quantity) {
        this.takenIn = takenIn;
        this.price = price;
        this.priceWithSale = priceWithSale;
        this.quantity = quantity;
    }
}
