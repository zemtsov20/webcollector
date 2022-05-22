package com.web.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BasicStatistic {
    private String earnings;

    private String period;

    private String salesCount;

}
