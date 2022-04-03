package com.common.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ParsedContentTsId implements Serializable {
    private Long productId;

    private Date takenIn;

    private Integer productPrice;
}
