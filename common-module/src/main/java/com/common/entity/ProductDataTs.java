package com.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(ProductDataTsId.class)
@Table(name = "product_data_ts")
public class ProductDataTs {
    @Id
    private Long productId;

    @Id
    @Column(name = "taken_in", nullable=false)
    private Date takenIn;

    @Column
    private Integer price;

    @Column
    private Integer priceWithSale;

    @Column
    private Integer quantity;
}
