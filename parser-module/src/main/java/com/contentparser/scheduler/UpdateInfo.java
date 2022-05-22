package com.contentparser.scheduler;

import com.common.entity.RawData;
import com.common.enums.State;
import com.common.repository.ProductDataRepository;
import com.common.repository.RawDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.common.utils.Constants.wbApiPrefix;
import static com.common.utils.Constants.wbQtyApiPrefix;

/**
 * Class that adds new tasks to update info
 *
 */
@Component
public class UpdateInfo {
    public static final Logger logger = LoggerFactory.getLogger(UpdateInfo.class);

    @Autowired
    private RawDataRepository rawDataRepo;

    @Autowired
    private ProductDataRepository productDataRepository;

    /**
     * Adds products to parse in database in 4 am every day
     *
     */
    @Scheduled(cron = "0 0 4 * * *")
    public void setProductsForTS() {
        var products = productDataRepository.findAll();
        if (products.isEmpty())
            return;
        products.forEach(product -> rawDataRepo
                .save(new RawData(wbQtyApiPrefix + product.getProductId().toString(), product.getCategoryUrl(), State.QUEUED)));

        logger.info(products.size() + " products queued for TS");
    }

    /**
     * Adds task to parse in database main site menu every week
     *
     */
    @Scheduled(cron = "@weekly")
    public void updateHierarchy() {
        rawDataRepo.save(new RawData(wbApiPrefix + "/api/menu/getburger?includeBrands=False", wbApiPrefix, State.QUEUED));
    }
}
