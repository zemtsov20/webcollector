package com.web.controllers;

import com.web.models.BasicStatistic;
import com.web.models.DonutStatistic;
import com.web.models.SubcategoryStatistic;
import com.web.services.MenuService;
import com.web.services.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class BasicController {

    @Autowired
    StatisticService statisticService;

    @Autowired
    MenuService menuService;

    @GetMapping("/basic-statistics")
    public ResponseEntity<List<BasicStatistic>> getBasicStatistics(@RequestParam(name="start") String start,
                                                                   @RequestParam(name="end") String end,
                                                                   @RequestParam(name="ref") String ref) throws ParseException {
        var basicStats = statisticService.getBasicStatistic(start, end, ref.replace("/api/catalog/", ""));
        return basicStats
                .map(s -> new ResponseEntity<>(s, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
    @GetMapping("/subcategory-statistics")
    public ResponseEntity<List<SubcategoryStatistic>> getSubcategoryStatistics(@RequestParam(name="start") String start,
                                                                              @RequestParam(name="end") String end,
                                                                              @RequestParam(name="ref") String ref) throws ParseException {
        var subcategoryStats = statisticService.getSubcategoryStatistic(start, end, ref);
        if (subcategoryStats.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(subcategoryStats, HttpStatus.OK);
    }
    @GetMapping("/donut-statistics")
    public ResponseEntity<List<DonutStatistic>> getDonutStatistics(@RequestParam(name="ref") String ref) {
        var donutStats = statisticService.getDonutStatistic(ref.replace("/api/catalog/", ""));
        if (donutStats.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(donutStats, HttpStatus.OK);
    }

    @GetMapping("/menu-json")
    public String getMenuJson() {
        return menuService.getMenuJson();
    }
}
