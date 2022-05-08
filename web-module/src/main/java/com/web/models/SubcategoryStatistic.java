package com.web.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SubcategoryStatistic {
    private List<BasicStatistic> statistics;
    private String categoryUrl;
}
