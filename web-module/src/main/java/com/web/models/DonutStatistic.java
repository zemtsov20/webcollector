package com.web.models;

import com.common.utils.SqlDonutStatistic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class DonutStatistic {
    private String brandName;

    private Long productCount;

    public DonutStatistic(SqlDonutStatistic sqlDonutStatistic) {
        this.brandName = sqlDonutStatistic.getBrandName();
        this.productCount = sqlDonutStatistic.getProductCount();
    }
}
