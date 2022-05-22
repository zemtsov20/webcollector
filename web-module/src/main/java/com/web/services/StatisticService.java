package com.web.services;

import com.common.entity.CategoryData;
import com.common.entity.ProductData;
import com.common.entity.ProductDataTs;
import com.common.repository.CategoryDataRepository;
import com.common.repository.ProductDataRepository;
import com.common.repository.ProductDataTsRepository;
import com.web.models.BasicStatistic;
import com.web.models.DonutStatistic;
import com.web.models.SubcategoryStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticService {
    @Autowired
    private CategoryDataRepository categoryDataRepo;

    @Autowired
    private ProductDataRepository productDataRepo;

    @Autowired
    private ProductDataTsRepository productDataTsRepo;

    public Optional<List<BasicStatistic>> getBasicStatistic(String startDateStr, String endDateStr, String categoryUrl) throws ParseException {
        int     accuracy = 20;
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
        DateFormat frontDateFormat = new SimpleDateFormat("dd MM yyyy HH:mm:ss Z");
        long    start = dateFormat.parse(startDateStr).getTime(),
                end = dateFormat.parse(endDateStr).getTime();
        long    diff = (end - start) / accuracy;
        List<Long> ids = productDataRepo
                .findAllByCategoryUrlContaining(categoryUrl)
                .stream().map(ProductData::getProductId)
                .collect(Collectors.toList());
        if (ids.isEmpty())
            return Optional.empty();
        long[]  earnings = new long[accuracy],
                sales = new long[accuracy];

         for (int i = 0; i < accuracy; i++) {
            end = start + diff;
            var productsTsBetweenDate = productDataTsRepo
                    .findByProductIdsBetweenDate(new Date(start), new Date(end), ids);
            for (Long id : ids) {
                var oneProductTsList = productsTsBetweenDate.stream()
                        .filter(p -> p.getProductId().equals(id))
                        .collect(Collectors.toList());
                earnings[i] += calcEarnings(oneProductTsList);
                sales[i] += countSales(oneProductTsList);
            }
            start = end;
        }

//        // counting avg
//        long avgEarnings = 0;
//        long avgSales = 0;
//        if (Arrays.stream(earnings).sum() > 0) {
//            avgEarnings = Arrays.stream(earnings).sum() / Arrays.stream(earnings).filter(e -> e > 0).count();
//            avgSales = Arrays.stream(sales).sum() / Arrays.stream(sales).filter(s -> s > 0).count();
//        }
//
//        // filling empty spaces with avg
//        long finalAvgSales = avgSales;
//        long finalAvgEarnings = avgEarnings;
//        sales = Arrays.stream(sales).map(s -> s == 0 ? finalAvgSales : s).toArray();
//        earnings = Arrays.stream(earnings).map(e -> e == 0 ? finalAvgEarnings : e).toArray();
        long[] smaEarnings = new long[accuracy];
        long[] smaSales = new long[accuracy];
        smaEarnings[0] = Math.round(Arrays.stream(earnings).average().orElse(0));
        smaSales[0] = Math.round(Arrays.stream(sales).average().orElse(0));
        for (int i = 1; i < accuracy; i++) {
            smaEarnings[i] = smaEarnings[i - 1] - earnings[i - 1] + earnings[i];
            smaSales[i] = smaSales[i - 1] - sales[i - 1] + sales[i];
        }
        start = dateFormat.parse(startDateStr).getTime() + diff;
        List<BasicStatistic> basicStatistics = new ArrayList<>();
        for (int i = 1; i < accuracy - 1; i++) {
            end = start + diff;
            basicStatistics.add(new BasicStatistic(
                    String.valueOf(smaEarnings[i]),
                    frontDateFormat.format(new Date(start)),
                    String.valueOf(smaSales[i])));
            start = end;
        }

        return Optional.of(basicStatistics);
    }

    private String avg(long... arr) {
        return String.valueOf(Math.round(Arrays.stream(arr).average().orElse(0)));
    }

    private long calcEarnings(List<ProductDataTs> oneProductTsList) {
        long earnings = 0L;
        for (int i = 1; i < oneProductTsList.size(); i++) {
            long sales = oneProductTsList.get(i - 1).getQuantity() - oneProductTsList.get(i).getQuantity();
            if (sales >= 0) {
                earnings += sales * oneProductTsList.get(i).getPriceWithSale();
            }
        }

        return earnings;
    }

    private long countSales(List<ProductDataTs> oneProductTsList) {
        int productSales = 0;
        for (int i = 1; i < oneProductTsList.size(); i++) {
            long sales = oneProductTsList.get(i - 1).getQuantity() - oneProductTsList.get(i).getQuantity();
            if (sales >= 0) {
                productSales +=  sales;
            }
        }

        return productSales;
    }


    public List<SubcategoryStatistic> getSubcategoryStatistic(String startDateStr, String endDateStr, String categoryUrl) throws ParseException {
        List<SubcategoryStatistic> subcategoryStatistics = new ArrayList<>();
        if (categoryDataRepo.findTopByPageUrl(categoryUrl).isPresent()) {
            CategoryData categoryData = categoryDataRepo.findTopByPageUrl(categoryUrl).get();
            var subcategoryDataList = categoryDataRepo.findAllByParentId(categoryData.getId());
            if (subcategoryDataList.isEmpty())
                getBasicStatistic(startDateStr, endDateStr, categoryUrl.replace("/api/catalog/", ""))
                        .ifPresent(e -> subcategoryStatistics.add(new SubcategoryStatistic(e, categoryData.getName())));
            for (var subcategoryData : subcategoryDataList) {
                getBasicStatistic(startDateStr, endDateStr, subcategoryData.getPageUrl().replace("/api/catalog/", ""))
                        .ifPresent(e -> subcategoryStatistics.add(new SubcategoryStatistic(e, subcategoryData.getName())));
            }
        }

        return subcategoryStatistics;
    }

    public List<DonutStatistic> getDonutStatistic(String categoryUrl) {
        return productDataRepo.findDistinctCountByBrandNameContaining(categoryUrl)
                .stream()
                .map(DonutStatistic::new)
                .collect(Collectors.toList());
    }
}
