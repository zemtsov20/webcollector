package com.contentparser.scheduler;

import com.common.entity.ParsedContent;
import com.common.enums.HtmlState;
import com.common.repository.UrlDataRepository;

import com.contentparser.beans.GetContentFromHtml;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledHtmlParser {
    private static final Logger logger = LoggerFactory.getLogger(
            ScheduledHtmlParser.class);

    @Autowired
    private GetContentFromHtml getContentFromHtml;
    @Autowired
    private UrlDataRepository urlDataRepository;

    //@Scheduled(fixedDelay = 1000 * 10)
    public void getContent() {
        var htmlList = urlDataRepository.findByState(HtmlState.UNCHECKED);
        logger.info("Scheduling in parser is working.");

        htmlList.forEach(html -> {
            html.setState(HtmlState.CHECKING);
            urlDataRepository.save(html);
            if(html.getText() == null) {
                urlDataRepository.delete(html);
            }
            else {
                html.setParsedContent(getContentFromHtml.productFromPage(html.getText())
                        .orElse(new ParsedContent()));
                html.setState(HtmlState.CHECKED);
                urlDataRepository.save(html);
            }
        });
    }
}
