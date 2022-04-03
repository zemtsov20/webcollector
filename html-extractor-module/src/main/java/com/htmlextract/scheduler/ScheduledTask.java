package com.htmlextract.scheduler;

import com.common.entity.PageUrl;
import com.common.entity.ParsedContent;
import com.common.enums.State;
import com.common.repository.ParsedContentRepository;
import com.common.repository.PageUrlRepository;
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
    private PageUrlRepository pageUrlRepository;

    @Autowired
    private ParsedContentRepository parsedContentRepository;

    @Autowired
    private GetHtml getHtml;

    @Scheduled(fixedDelay = 1000 * 10)
    public void getCategoriesUrls() {
        logger.info("Scheduling in htmlextract is working.");
        for (PageUrl pageUrl : pageUrlRepository.findByState(State.QUEUED)) {
            downloadCategoryHtml(pageUrl);
            logger.info("Got info from " + pageUrl.getUrl());
        }

        for (ParsedContent parsedContent : parsedContentRepository.findByState(State.QUEUED)) {
            downloadProductHtml(parsedContent);
            logger.info("Got product info from " + parsedContent.getUrl());
        }
    }
    // TODO copy-pasted, idk how to DRY it
    private void downloadProductHtml(ParsedContent parsedContent) {
        parsedContent.setState(State.DOWNLOADING);
        parsedContentRepository.save(parsedContent);
        String html = getHtml.getHtmlByUrl(parsedContent.getUrl());
        if (html.isEmpty())
            parsedContent.setState(State.DOWNLOADING_ERROR);
        else {
            parsedContent.setHtml(html);
            parsedContent.setState(State.DOWNLOADED);
        }
        parsedContentRepository.save(parsedContent);
    }

    private void downloadCategoryHtml(PageUrl pageUrl) {
        pageUrl.setState(State.DOWNLOADING);
        pageUrlRepository.save(pageUrl);
        String html = getHtml.getHtmlByUrl(pageUrl.getUrl());
        if (html.isEmpty())
            pageUrl.setState(State.DOWNLOADING_ERROR);
        else {
            pageUrl.setHtml(html);
            pageUrl.setState(State.DOWNLOADED);
        }
        pageUrlRepository.save(pageUrl);
    }


}
