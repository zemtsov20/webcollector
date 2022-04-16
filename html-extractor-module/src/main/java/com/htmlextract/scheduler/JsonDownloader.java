package com.htmlextract.scheduler;

import com.common.entity.CategoryData;
import com.common.entity.ProductData;
import com.common.entity.SiteData;
import com.common.enums.State;
import com.common.repository.CategoryDataRepository;
import com.common.repository.ProductDataRepository;
import com.common.repository.SiteDataRepository;
import com.htmlextract.beans.GetHtml;
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
public class JsonDownloader {
    private static final Logger logger = LoggerFactory.getLogger(JsonDownloader.class);
    private static final String wbApiPrefix = "https://napi.wildberries.ru";

    @Autowired
    private GetHtml getHtml;

    @Autowired
    private SiteDataRepository siteDataRepo;

    @Autowired
    private CategoryDataRepository categoryDataRepo;

    @Autowired
    private ProductDataRepository productDataRepository;

    //@Scheduled(fixedDelay = 1000 * 60 * 60 * 24 * 7)
    public void getHierarchyJson() {
        String json = getHtml.getResponse( wbApiPrefix + "/api/menu/getburger?includeBrands=False");
        if (json.isEmpty()) {
            siteDataRepo.save(new SiteData(new Date(), State.DOWNLOADING_ERROR, null));
            logger.error("Hierarchy JSON downloading error.");
        }
        else {
            siteDataRepo.save(new SiteData(new Date(), State.DOWNLOADED, json));
            logger.info("Hierarchy JSON downloaded successfully.");
        }
    }

    @Transactional
    //@Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    public void getCategoryJson() {
        int count = 0;
        logger.info("Category JSONs downloading.");
        for (CategoryData categoryData : categoryDataRepo.findByState(State.QUEUED)) {
            categoryData.setState(State.DOWNLOADING);
            categoryDataRepo.save(categoryData);
            String json = getHtml.getResponse(wbApiPrefix  + categoryData.getPageUrl()
                                                            + "?page="
                                                            + categoryData.getPageToParse());
            if (json.isEmpty()) {
                categoryData.setState(State.DOWNLOADING_ERROR);
                categoryDataRepo.save(categoryData);
            }
            else {
                categoryData.setJson(json);
                categoryData.setState(State.DOWNLOADED);
                categoryDataRepo.save(categoryData);
                count++;
            }
            logger.info(categoryData.getPageUrl() + " prepared, state: " + categoryData.getState());
        }
        logger.info(count + " category JSONs downloaded successfully.");
    }

    @Transactional
    @Scheduled(fixedDelay = 1000 * 60 * 60)
    public void getProductJson() {
        int count = 0;
        logger.info("Product JSONs downloading.");
        for (ProductData productData : productDataRepository.findByState(State.QUEUED, PageRequest.of(0, 20))) {
            productData.setState(State.DOWNLOADING);
            productDataRepository.save(productData);
            String json = getHtml.getResponse("https://wbxcatalog-ru.wildberries.ru/nm-2-card/catalog" +
                    "?spp=0&regions=64,83,4,38,33,70,82,69,86,75,30,1,40,22,31,66,48,80,71,68" +
                    "&stores=117673,122258,122259,125238,125239,125240,6159,507,3158,117501,120602,120762,6158,121709,124731,159402,2737,130744,117986,1733,686,132043" +
                    "&pricemarginCoeff=1.0&reg=0&appType=1&offlineBonus=0&onlineBonus=0&emp=0&locale=ru&lang=ru&curr=rub" +
                    "&couponsGeo=12,3,18,15,21&dest=-1029256,-102269,-1278703,-1255563&nm=" + productData.getProductId());
            if (json.isEmpty()) {
                productData.setState(State.DOWNLOADING_ERROR);
                productDataRepository.save(productData);
            }
            else {
                productData.setJson(json);
                productData.setState(State.DOWNLOADED);
                productDataRepository.save(productData);
                count++;
            }
        }

        logger.info(count + " product JSONs downloaded successfully.");
    }


}
