package com.htmlextract.scheduler;

import com.common.entity.RawData;
import com.common.enums.State;
import com.common.repository.RawDataRepository;
import com.common.repository.ProductDataRepository;
import com.htmlextract.beans.WebConnector;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static com.common.utils.Constants.wbApiPrefix;
import static com.common.utils.Constants.wbQtyApiPrefix;

/**
 * Class for downloading JSONs
 */
@Component
@RequiredArgsConstructor
public class JsonDownloader {
    private static final Logger logger = LoggerFactory.getLogger(JsonDownloader.class);

    @Autowired
    private WebConnector webConnector;

    @Autowired
    private RawDataRepository rawDataRepo;

    /**
     * Every 250ms locks one row from RawData table and downloading info by link
     */
    @Transactional
    @Scheduled(fixedDelay = 250)
    public void getJson() {
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
}
