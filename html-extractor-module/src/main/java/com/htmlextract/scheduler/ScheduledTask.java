package com.htmlextract.scheduler;

import com.common.entity.Url;
import com.common.repository.UrlDataRepository;
import com.common.repository.UrlRepository;
import com.htmlextract.beans.GetHtml;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTask {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UrlDataRepository urlDataRepository;

    @Autowired
    private GetHtml getHtml;

    @Scheduled(fixedDelay = 1000 * 10)
    public void getContentFromAllHtmls() {
        logger.info("Scheduling in htmlextract is working.");
        for (Url url : urlRepository.findAll()) {
            urlDataRepository.save(
                    getHtml.getHtmlByUrl(url.getUrl()));
            logger.info("Got info from " + url.getUrl());
        }
    }
}
