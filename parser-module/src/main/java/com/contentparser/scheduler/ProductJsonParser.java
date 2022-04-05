package com.contentparser.scheduler;

import com.common.entity.ProductData;
import com.common.entity.ProductDataTs;
import com.common.enums.State;
import com.common.repository.ProductDataRepository;
import com.contentparser.beans.ProductParse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class ProductJsonParser {
    private static final Logger logger = LoggerFactory.getLogger(ProductJsonParser.class);

    @Autowired
    private ProductDataRepository productDataRepo;

    @Autowired
    private ProductParse productParse;

    @Transactional
    @Scheduled(fixedDelay = 1000 * 60 * 60)
    public void getProductInfo() {
        for (ProductData productData : productDataRepo.findByState(State.DOWNLOADED, PageRequest.of(0, 10))) {
            productData.setState(State.PARSING);
            productDataRepo.save(productData);
            if (productData.getName() == null)
                productParse.getProductInfo(productData);
            productData.getProductDataTs().add(productParse.getProductTsInfo(productData.getJson()));
            productDataRepo.save(productData);
        }
        logger.info("Products parsing ended.");
    }

}
