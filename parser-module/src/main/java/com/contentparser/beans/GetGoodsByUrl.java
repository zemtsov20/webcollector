package com.contentparser.beans;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class GetGoodsByUrl {

    @Autowired
    private GetUrlsFromHtml getUrlsFromHtml;


    public Set<String> getAllByCategoryUrl(String parsingFrom) throws IOException {
        Optional<Set<String>> urls;
        Set<String> urlsSet = new HashSet<>();
        int i = 1;
        String html = IOUtils.toString(new URL(parsingFrom), StandardCharsets.UTF_8);
        while ((urls = getUrlsFromHtml.goodsUrlsFromPage(html)).isPresent()) {
            //urls.get().forEach(url -> pageUrlRepository.save(new PageUrl(System.currentTimeMillis(), url, parsingFrom)));
            urlsSet.addAll(urls.get());
            html = IOUtils.toString(new URL(parsingFrom + "?page=" + ++i), StandardCharsets.UTF_8);
        }

        return urlsSet;
    }

    public Set<String> getFromPage(String html) {
        Optional<Set<String>> urls;
        Set<String> urlsSet = new HashSet<>();
        int i = 1;
        while ((urls = getUrlsFromHtml.goodsUrlsFromPage(html)).isPresent()) {
            urlsSet.addAll(urls.get());
        }

        return urlsSet;
    }
}
