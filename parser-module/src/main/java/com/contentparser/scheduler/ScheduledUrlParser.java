package com.contentparser.scheduler;

import com.common.entity.Url;
import com.common.repository.UrlRepository;
import com.contentparser.beans.GetUrlsFromHtml;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ScheduledUrlParser {
    private static final Logger logger = LoggerFactory.getLogger(
            ScheduledUrlParser.class);

    @Autowired
    private GetUrlsFromHtml getUrlsFromHtml;

    @Autowired
    private UrlRepository urlRepository;

    @Scheduled(fixedDelay = 1000 * 10)
    public void getUrls() throws IOException {
        logger.info("Scheduling in url parser is working.");
        // TODO: hardcoded, fix this part
        String parsingFrom = "https://www.wildberries.ru/catalog/elektronika/tehnika-dlya-doma/telefony-statsionarnye";
        String html = IOUtils.toString(new URL(parsingFrom), StandardCharsets.UTF_8);


        Optional<Set<String>> urls;
        int i = 1;
        while ((urls = getUrlsFromHtml.urlsFromPage(html)).isPresent()) {
            urls.get().forEach(url -> urlRepository.save(new Url(url)));
            html = IOUtils.toString(new URL(parsingFrom + "&page=" + ++i), StandardCharsets.UTF_8);
        }
    }

}
