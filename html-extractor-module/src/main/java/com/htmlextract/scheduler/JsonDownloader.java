package com.htmlextract.scheduler;

import com.common.entity.RawData;
import com.common.entity.SiteData;
import com.common.enums.State;
import com.common.repository.RawDataRepository;
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

import static com.common.utils.Constants.wbApiPrefix;
import static com.common.utils.Constants.wbQtyApiPrefix;

@Component
@RequiredArgsConstructor
public class JsonDownloader {
    private static final Logger logger = LoggerFactory.getLogger(JsonDownloader.class);

    @Autowired
    private WebConnector webConnector;

    @Autowired
    private RawDataRepository rawDataRepo;

    @Autowired
    private ProductDataRepository productDataRepository;

    @PostConstruct
    public void testCategoryDownload() {
        //rawDataRepo.save(new RawData(wbApiPrefix + "/api/catalog/sport/vidy-sporta/velosport", State.QUEUED));
        rawDataRepo.save(new RawData(wbApiPrefix + "/api/menu/getburger?includeBrands=False", wbApiPrefix, State.QUEUED));
        rawDataRepo.save(new RawData(wbApiPrefix + "/api/catalog/aksessuary/avtotovary", wbApiPrefix, State.QUEUED));
    }

    @Transactional
    @Scheduled(fixedDelay = 500)
    public void getCategoryJson() {
        var rawDataList = rawDataRepo.findAndLockByState(State.QUEUED, PageRequest.of(0, 1));
        if (rawDataList.isEmpty())
            return;
        for (RawData rawData : rawDataList) {
            String json = webConnector.getResponse(rawData.getDataRef());
            if (json.isEmpty()) {
                rawData.setState(State.DOWNLOADING_ERROR);
            }
            else {
                rawData.setJson(json);
                rawData.setState(State.DOWNLOADED);
            }
            rawDataRepo.save(rawData);
            logger.info(rawData.getDataRef() + " prepared, state: " + rawData.getState());
        }
    }

    // TODO extract next methods to another service (or set different schedule?)
    //@Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    public void setProductsForTS() {
        var products = productDataRepository.findAll();
        if (products.isEmpty())
            return;
        products.forEach(product -> rawDataRepo
                .save(new RawData(wbQtyApiPrefix + product.getProductId().toString(), product.getCategoryUrl(), State.QUEUED)));

        logger.info(products.size() + " products queued for TS");
    }

    //@Scheduled(fixedDelay = 1000 * 60 * 60 * 24 * 7)
    public void updateHierarchy() {
        rawDataRepo.save(new RawData(wbApiPrefix + "/api/menu/getburger?includeBrands=False", wbApiPrefix, State.QUEUED));
    }
}
