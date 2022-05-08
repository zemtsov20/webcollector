package com.web.services;

import com.common.entity.ProductData;
import com.common.repository.ProductDataRepository;
import com.common.repository.ProductDataTsRepository;
import com.common.utils.SqlDonutStatistic;
import com.web.models.BasicStatistic;
import com.web.models.DonutStatistic;
import com.web.models.SubcategoryStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.common.utils.Constants.wbApiPrefix;

@Service
public class StatisticService {
    @Autowired
    private ProductDataRepository productDataRepo;

    @Autowired
    private ProductDataTsRepository productDataTsRepo;

    public List<BasicStatistic> getBasicStatistic(String startDateStr, String endDateStr, String categoryUrl) throws ParseException {
        int     accuracy = 20;
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
        DateFormat frontDateFormat = new SimpleDateFormat("dd MM yyyy HH:mm:ss Z");
        Date    start = dateFormat.parse(startDateStr),
                end = dateFormat.parse(endDateStr);
        long diff = (end.getTime() - start.getTime()) / accuracy;
        List<Long> ids = productDataRepo
                .findAllByCategoryUrlContaining(categoryUrl)
                .stream().map(ProductData::getProductId)
                .collect(Collectors.toList());
        List<BasicStatistic> basicStatistics = new ArrayList<>();
        for (int i = 0; i < accuracy; i++) {
            end = new Date(start.getTime() + diff);
            Double avgPrice = productDataTsRepo.findAvgPriceByProductIdsBetweenDate(start, end, ids) ;
            Double avgQuantity = productDataTsRepo.findAvgQuantityByProductIdsBetweenDate(start, end, ids);
            basicStatistics.add(new BasicStatistic(avgPrice == null ? "null" : String.format("%.0f", avgPrice), frontDateFormat.format(start),
                    avgQuantity == null ? "null" : String.format("%.0f", avgQuantity)));
            start = end;
        }

        return basicStatistics;
    }

    public List<SubcategoryStatistic> getSubcategoryStatistic(String startDateStr, String endDateStr, String categoryUrl) throws ParseException {
        List<SubcategoryStatistic> subcategoryStatistics = new ArrayList<>();
        for (var subcategoryUrl : productDataRepo.findDistinctByCategoryUrlContaining(categoryUrl)) {
            subcategoryStatistics.add(new SubcategoryStatistic(getBasicStatistic(startDateStr, endDateStr, subcategoryUrl),
                    subcategoryUrl.replace(wbApiPrefix + categoryUrl + "/", "")));
        }
        return subcategoryStatistics;
    }

    public List<DonutStatistic> getDonutStatistic(String categoryUrl) {
        return productDataRepo.findDistinctCountByBrandNameContaining(categoryUrl).stream().map(DonutStatistic::new).collect(Collectors.toList());
    }
}
