package com.web.controllers;

import com.common.entity.ProductData;
import com.common.enums.State;
import com.common.repository.ProductDataRepository;
import com.common.repository.ProductDataTsRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class BasicController {

    @Autowired
    private ProductDataRepository productDataRepo;

    @Autowired
    private ProductDataTsRepository productDataTsRepo;

    @Transactional
    @GetMapping("/basic")
    public List<BasicStatistic> getBasicStatistic(@RequestParam(name="period") String period, @RequestParam(name="name") String name) {

        return productDataRepo.findByState(State.PARSED, PageRequest.of(0, 5))
                .stream()
                .map(productData ->
                        new BasicStatistic(productData.getProductId().toString(), period ,productData.getName()))
                .collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    private static class BasicStatistic {
        private String averagePrice;

        private String period;

        private String goods;
    }
}
