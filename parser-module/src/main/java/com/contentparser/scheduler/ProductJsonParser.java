package com.contentparser.scheduler;

import com.common.entity.ProductData;
import com.common.entity.ProductDataTs;
import com.common.enums.State;
import com.common.repository.ProductDataRepository;
import com.contentparser.beans.ProductParse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

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
        for (ProductData productData : productDataRepo.findByState(State.DOWNLOADED, PageRequest.of(0, 5))) {
            productData.setState(State.PARSING);
            productData = productDataRepo.save(productData);

            // if main product info not parsed yet, then parse
            if (productData.getName() == null)
                productParse.getProductInfo(productData);

            ProductDataTs productDataTs = productParse.getProductTsInfo(productData.getJson());
            productDataTs.setProductData(productData);

            productData.getProductDataTsList().add(productDataTs);
            productDataRepo.save(productData);
        }
        logger.info("Products parsing ended.");
    }

}
