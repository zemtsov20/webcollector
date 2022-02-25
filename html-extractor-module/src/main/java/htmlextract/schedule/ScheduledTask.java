package htmlextract.schedule;

import common.repository.UrlDataEntityRepository;
import common.repository.UrlEntityRepository;
import htmlextract.beans.GetHtml;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTask {

//    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    private UrlEntityRepository urlEntityRepository;
    @Autowired
    private UrlDataEntityRepository urlDataEntityRepository;
    @Autowired
    private GetHtml getHtml;

    @Scheduled(fixedDelay = 1000 * 10)
    public void getHtml() {
        if(urlEntityRepository.findById(2L).isPresent()) {
            urlDataEntityRepository.
                    save(getHtml.
                            getHtmlByUrl(urlEntityRepository.
                                    findById(2L).get().getUrl()));
        }

    }
}
