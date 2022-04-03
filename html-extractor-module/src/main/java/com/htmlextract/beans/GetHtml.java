package com.htmlextract.beans;


import com.common.entity.ParsedContent;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
@NoArgsConstructor
public class GetHtml {
    public String getHtmlByUrl(String url) {
        String htmlContent = "";
        try {
            htmlContent = IOUtils.toString(new URL(url), StandardCharsets.ISO_8859_1);
        } catch (MalformedURLException e) {
            System.out.println("Wrong url! " + e.getMessage() + " for url " + url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return htmlContent;
    }
}
