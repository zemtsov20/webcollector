package com.contentparser.beans;

import com.common.patterns.wb.WildberriesPatterns;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;

@Component
public class GetUrlsFromHtml {
    public Optional<Set<String>> urlsFromPage(String pageHtml) {
        Set<String> urls = new HashSet<>();

        Matcher wbGoodsUrlMatcher = WildberriesPatterns.WB_GOODS_URL.matcher(pageHtml);
        while(wbGoodsUrlMatcher.find()) {
            urls.add("https://www.wildberries.ru" + wbGoodsUrlMatcher.group(2));
        }

        return urls.isEmpty() ? Optional.empty() : Optional.of(urls);
    }
}
