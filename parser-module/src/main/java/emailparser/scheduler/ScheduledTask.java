package emailparser.scheduler;

import common.enums.HtmlState;
import common.repository.UrlDataEntityRepository;

import emailparser.beans.GetMailsFromHtml;
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
    private GetMailsFromHtml getMailsFromHtml;
    @Autowired
    private UrlDataEntityRepository urlDataEntityRepository;

    @Scheduled(fixedDelay = 1000)
    public void getEmails() {
        var htmlList = urlDataEntityRepository.findUnchecked();
        logger.info("Scheduling in parser is working.");
        if(!htmlList.isEmpty()) {
            htmlList.forEach(html -> {
                if(html.getText() != null) {
                    //html.setState(HtmlState.CHECKING);
                    html.setEmailsOnPage(getMailsFromHtml.emailsFromPage(html.getText()));
                    html.setState(HtmlState.CHECKED);
                    // make it updating, not saving new
                    urlDataEntityRepository.save(html);
                }
            });
        }

    }
}
