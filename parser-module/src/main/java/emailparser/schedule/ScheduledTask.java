package emailparser.schedule;

import common.enums.HtmlState;
import common.entity.UrlDataEntity;
import common.repository.UrlDataEntityRepository;

import emailparser.beans.GetMailsFromHtmlBean;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTask {

    @Autowired
    private GetMailsFromHtmlBean getMailsFromHtmlBean;
    @Autowired
    private UrlDataEntityRepository urlDataEntityRepository;

    @Scheduled(fixedDelay = 1000)
    public void getEmails() {
        var htmlList = urlDataEntityRepository.findUnchecked();
        if(!htmlList.isEmpty()) {
            htmlList.forEach(html -> {
                if(html.getText() != null) {
                    //html.setState(HtmlState.CHECKING);
                    html.setEmailsOnPage(getMailsFromHtmlBean.emailsFromPage(html.getText()));
                    html.setState(HtmlState.CHECKED);
                    // make it updating, not saving new
                    urlDataEntityRepository.save(html);
                }
            });
        }

    }
}
