package com.common.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
public class ProductDataTsId implements Serializable {
    private Long productId;

    private Date takenIn;
}
