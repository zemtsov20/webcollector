package com.htmlextract.beans;


import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Class for downloading info by link
 */
@Component
@NoArgsConstructor
public class WebConnector {
    /**
     * Downloading info by input link
     * @param url Link to prepare
     * @return Result in String format
     */
    public String getResponse(String url) {
//        try {
//            Thread.sleep((long) (Math.random() * 100));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        String content = "";
        try {
            content = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
        } catch (MalformedURLException e) {
            System.out.println("Wrong url! " + e.getMessage() + " for url " + url);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return content;
    }
}
