package com.mailparser.mailextract.schedule;

import com.mailparser.mailextract.beans.GetEmailsFromHtmlBean;
import com.mailparser.mailextract.enums.HtmlState;
import com.mailparser.mailextract.repository.UrlDataEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTask {

    @Autowired
    private GetEmailsFromHtmlBean getMailsFromHtmlBean;
    @Autowired
    private UrlDataEntityRepository urlDataEntityRepository;

    @Scheduled(fixedRate = 1000)
    public void getEmails() {
        var html = urlDataEntityRepository.findFirstUnchecked().get(0);
        if(html.getText() != null) {
            //html.setState(HtmlState.CHECKING);
            html.setEmailsOnPage(getMailsFromHtmlBean.emailsFromPage(html.getText()));
            html.setState(HtmlState.CHECKED);
            // make it updating, not saving new
            urlDataEntityRepository.save(html);
        }
    }
}
