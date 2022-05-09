package com.web.controllers;

import com.common.enums.State;
import com.web.models.BasicStatistic;
import com.web.models.DonutStatistic;
import com.web.models.SubcategoryStatistic;
import com.web.services.MenuService;
import com.web.services.StatisticService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class BasicController {

    @Autowired
    StatisticService statisticService;

    @Autowired
    MenuService menuService;

    @GetMapping("/basic-statistics")
    public List<BasicStatistic> getBasicStatistics(@RequestParam(name="start") String start,
                                                   @RequestParam(name="end") String end,
                                                   @RequestParam(name="ref") String ref) throws ParseException {
        return statisticService.getBasicStatistic(start, end, ref.replace("/api/catalog/", ""));
    }
    @GetMapping("/subcategory-statistics")
    public List<SubcategoryStatistic> getFullStatistics(@RequestParam(name="start") String start,
                                                        @RequestParam(name="end") String end,
                                                        @RequestParam(name="ref") String ref) throws ParseException {
        return statisticService.getSubcategoryStatistic(start, end, ref);
    }
    @GetMapping("/donut-statistics")
    public List<DonutStatistic> getDonutStatistics(@RequestParam(name="ref") String ref) {
        return statisticService.getDonutStatistic(ref.replace("/api/catalog/", ""));
    }

    @GetMapping("/menu-json")
    public String getMenuJson() {
        return menuService.getMenuJson();
    }
}
