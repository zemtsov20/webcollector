package com.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class SqlDonutStatistic {
    private String brandName;
    private Long productCount;
}
