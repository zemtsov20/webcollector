package com.web.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BasicStatistic {
    private String averagePrice;

    private String period;

    private String goodsCount;

}
