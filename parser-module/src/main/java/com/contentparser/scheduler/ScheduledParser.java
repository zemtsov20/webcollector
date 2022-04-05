package com.contentparser.scheduler;

import com.common.entity.PageUrl;
import com.common.entity.ParsedContent;
import com.common.enums.State;
import com.common.repository.PageUrlRepository;
import com.common.repository.ParsedContentRepository;
import com.contentparser.beans.GetContentFromHtml;
import com.contentparser.beans.GetGoodsByUrl;
import com.contentparser.beans.GetUrlsFromHtml;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ScheduledParser {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledParser.class);

    @Autowired
    private GetContentFromHtml getContentFromHtml;

    @Autowired
    private GetGoodsByUrl getGoodsByUrl;

    @Autowired
    private GetUrlsFromHtml getUrlsFromHtml;

    @Autowired
    private PageUrlRepository pageUrlRepository;

    @Autowired
    private ParsedContentRepository parsedContentRepository;

    @Transactional
    //@Scheduled(fixedDelay = 1000 * 5)
    public void getUrls() {
        logger.info("Scheduling in url parser is working.");
        if (pageUrlRepository.findAll().isEmpty()) {
            pageUrlRepository.save(new PageUrl(
                            System.currentTimeMillis(),
                            "https://www.wildberries.ru",
                            null, false, State.QUEUED));
        }
        List<PageUrl> uncheckedUrls;

        while (!(uncheckedUrls = pageUrlRepository.findByStateAndChild(State.DOWNLOADED, false)).isEmpty()) {
            for (PageUrl uncheckedUrl : uncheckedUrls) {
                uncheckedUrl.setState(State.PARSING);
                pageUrlRepository.save(uncheckedUrl);
                Set<String> categoryLinks;
                if (uncheckedUrl.getParentUrl() != null)
                    categoryLinks = getUrlsFromHtml.categoryUrlsFromPage(uncheckedUrl.getHtml());
                else
                    categoryLinks = getUrlsFromHtml.categoryUrlsFromMainPage(uncheckedUrl.getHtml());
                if (categoryLinks.isEmpty()) {
                    uncheckedUrl.setHasNoChild(true);
                    uncheckedUrl.setState(State.PARSED);
                    pageUrlRepository.save(uncheckedUrl);
                }
                else {
                    boolean hasNoChild = uncheckedUrl.getHtml().contains("hasnochild");
                    for (String categoryLink : categoryLinks) {
                        try {
                            pageUrlRepository.save(new PageUrl(
                                    System.currentTimeMillis(),
                                    categoryLink,
                                    uncheckedUrl.getUrl(),
                                    hasNoChild, State.QUEUED));
                        }
                        catch (DataIntegrityViolationException e) {
                            logger.error("Skipped already existing url");
                        }
                    }
                    pageUrlRepository.delete(uncheckedUrl);
                }
                logger.info(uncheckedUrl.getUrl() + " checked, status: " + uncheckedUrl.getState());
            }
        }
        logger.info("Urls category parsing is ended.");
        // extracting goods urls from category pages
        for (PageUrl pageUrl : pageUrlRepository.findByStateAndChild(State.DOWNLOADED, true)) {
            for (String productUrl : getGoodsByUrl.getFromPage(pageUrl.getHtml())) {
                parsedContentRepository.save(new ParsedContent(productUrl));
            }
        }
        logger.info("Goods urls parsing is ended.");
        // extracting goods info
        for (ParsedContent parsedContent : parsedContentRepository.findByState(State.DOWNLOADED)) {
            parsedContent.setState(State.PARSING);
            parsedContentRepository.save(parsedContent);
            parsedContentRepository.save(getContentFromHtml.contentFromPage(parsedContent));
        }
        logger.info("Goods info parsing is ended.");
    }
}
