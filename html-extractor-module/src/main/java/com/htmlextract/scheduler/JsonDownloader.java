package com.htmlextract.scheduler;

import com.common.entity.CategoryData;
import com.common.entity.ProductData;
import com.common.entity.SiteData;
import com.common.enums.State;
import com.common.repository.CategoryDataRepository;
import com.common.repository.ProductDataRepository;
import com.common.repository.SiteDataRepository;
import com.htmlextract.beans.WebConnector;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JsonDownloader {
    private static final Logger logger = LoggerFactory.getLogger(JsonDownloader.class);
    private static final String wbApiPrefix = "https://napi.wildberries.ru";

    @Autowired
    private WebConnector webConnector;

    @Autowired
    private SiteDataRepository siteDataRepo;

    @Autowired
    private CategoryDataRepository categoryDataRepo;

    @Autowired
    private ProductDataRepository productDataRepository;

    //@Scheduled(fixedDelay = 1000 * 60 * 60 * 24 * 7)
    public void getHierarchyJson() {
        String json = webConnector.getResponse( wbApiPrefix + "/api/menu/getburger?includeBrands=False");
        if (json.isEmpty()) {
            siteDataRepo.save(new SiteData(new Date(), State.DOWNLOADING_ERROR, null));
            logger.error("Hierarchy JSON downloading error.");
        }
        else {
            siteDataRepo.save(new SiteData(new Date(), State.DOWNLOADED, json));
            logger.info("Hierarchy JSON downloaded successfully.");
        }
    }

    @PostConstruct
    public void testCategoryDownload() {
        categoryDataRepo.save(new CategoryData("/api/catalog/detyam/tovary-dlya-malysha/podguzniki/podguzniki-detskie?page=1", State.QUEUED));
    }

    @Transactional
    @Scheduled(fixedDelay = 1000 * 5)
    public void getCategoryJson() {
        int count = 0;
        logger.info("Category JSONs downloading.");
        for (CategoryData categoryData : categoryDataRepo.findByState(State.QUEUED, PageRequest.of(0, 5))) {
            categoryData.setState(State.DOWNLOADING);
            categoryDataRepo.save(categoryData);
            String json = webConnector.getResponse(wbApiPrefix  + categoryData.getPageUrl());
            if (json.isEmpty()) {
                categoryData.setState(State.DOWNLOADING_ERROR);
            }
            else {
                categoryData.setJson(json);
                categoryData.setState(State.DOWNLOADED);
                count++;
            }
            categoryDataRepo.save(categoryData);
            logger.info(categoryData.getPageUrl() + " prepared, state: " + categoryData.getState());
        }
        if (count > 0)
            logger.info(count + " category JSONs downloaded successfully.");
    }

    @Transactional
    @Scheduled(fixedDelay = 1000)
    public void getProductJson() {
        int count = 0;
        logger.info("Product JSONs downloading.");
        for (ProductData productData : productDataRepository.findByState(State.QUEUED, PageRequest.of(0, 10))) {
            productData.setState(State.DOWNLOADING);
            productDataRepository.save(productData);
            String json = webConnector.getResponse("https://wbxcatalog-ru.wildberries.ru/nm-2-card/catalog" +
                    "?spp=0&regions=64,83,4,38,33,70,82,69,86,75,30,1,40,22,31,66,48,80,71,68" +
                    "&stores=117673,122258,122259,125238,125239,125240,6159,507,3158,117501,120602,120762,6158,121709,124731,159402,2737,130744,117986,1733,686,132043" +
                    "&pricemarginCoeff=1.0&reg=0&appType=1&offlineBonus=0&onlineBonus=0&emp=0&locale=ru&lang=ru&curr=rub" +
                    "&couponsGeo=12,3,18,15,21&dest=-1029256,-102269,-1278703,-1255563&nm=" + productData.getProductId());
            if (json.isEmpty()) {
                productData.setState(State.DOWNLOADING_ERROR);
            }
            else {
                productData.setJson(json);
                productData.setState(State.DOWNLOADED);
                count++;
            }
            productDataRepository.save(productData);
        }
        if (count > 0)
            logger.info(count + " product JSONs downloaded successfully.");
    }

}
