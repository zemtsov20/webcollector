//package com.contentparser.scheduler;
//
//import com.common.entity.ProductData;
//import com.common.entity.ProductDataTs;
//import com.common.enums.State;
//import com.common.repository.ProductDataRepository;
//import com.common.repository.ProductDataTsRepository;
//import com.contentparser.beans.ProductParse;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.transaction.Transactional;
//import java.util.Random;
//
//@Component
//@RequiredArgsConstructor
//public class ProductJsonParser {
//    private static final Logger logger = LoggerFactory.getLogger(ProductJsonParser.class);
//
//    @Autowired
//    private ProductDataRepository productDataRepo;
//
//    @Autowired
//    private ProductDataTsRepository productDataTsRepo;
//
//    @Autowired
//    private ProductParse productParse;
//
//    @Transactional
//    @Scheduled(fixedDelay = 500)
//    public void getProductInfo() throws InterruptedException {
//        int counter = 0;
//        for (ProductData productData : productDataRepo.findByState(State.DOWNLOADED, PageRequest.of(0, 5))) {
//            // if main product info not parsed yet, then parse
//            if (productData.getName() == null)
//                productParse.getProductInfo(productData);
//
//            productData.setState(State.PARSED);
//
//            ProductDataTs productDataTs = productParse.getProductTsInfo(productData.getJson());
//            productDataTs.setProductId(productData.getProductId());
//
//            productDataTsRepo.save(productDataTs);
//
//            productDataRepo.save(productData);
//            counter++;
//        }
//        if (counter > 0)
//            logger.info(counter + " products parsed.");
//    }
//
//}
