package htmlextract.beans;


import common.entity.UrlDataEntity;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
public class GetHtml {

    public GetHtml() {}

    public UrlDataEntity getHtmlByUrl(String url) {
        String htmlContent = null;
        try {
            htmlContent =  IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
        } catch (MalformedURLException e) {
            System.out.println("Wrong url! " + e.getMessage() + " for url " + url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new UrlDataEntity(htmlContent);
    }

//    @PostConstruct
//    public void afterStart(){
////        while (true)
//        {
//            System.out.println("afterStart");
//        }
//    }
}
