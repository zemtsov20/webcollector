package com.contentparser.scheduler;

import com.common.entity.ParsedContent;
import com.common.enums.HtmlState;
import com.common.repository.UrlDataEntityRepository;

import com.contentparser.beans.GetContentFromHtml;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTask {
    private static final Logger logger = LoggerFactory.getLogger(
            ScheduledTask.class);

    @Autowired
    private GetContentFromHtml getContentFromHtml;
    @Autowired
    private UrlDataEntityRepository urlDataEntityRepository;

    @Scheduled(fixedDelay = 1000 * 10)
    public void getContent() {
        var htmlList = urlDataEntityRepository.findAll();
        logger.info("Scheduling in parser is working.");
        if(!htmlList.isEmpty()) {
            htmlList.forEach(html -> {
                if(html.getText() != null) {
                    //html.setState(HtmlState.CHECKING);
                    html.setParsedContent(getContentFromHtml.productFromPage(html.getText())
                                          .orElse(new ParsedContent()));
                    html.setState(HtmlState.CHECKED);
                    urlDataEntityRepository.save(html);
                }
            });
        }

    }
}
