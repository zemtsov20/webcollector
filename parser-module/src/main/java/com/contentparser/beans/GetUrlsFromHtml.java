package com.contentparser.beans;

import com.common.patterns.wb.WildberriesPatterns;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;

@Component
public class GetUrlsFromHtml {
    public Optional<Set<String>> goodsUrlsFromPage(String pageHtml) {
        Set<String> urls = new HashSet<>();

        Matcher wbGoodsUrlMatcher = WildberriesPatterns.WB_PRODUCT_URL.matcher(pageHtml);
        while(wbGoodsUrlMatcher.find()) {
            urls.add("https://www.wildberries.ru" + wbGoodsUrlMatcher.group(2));
        }

        return urls.isEmpty() ? Optional.empty() : Optional.of(urls);
    }

    public Set<String> categoryUrlsFromPage(String pageHtml) {
        Matcher categoryBlockMatcher = WildberriesPatterns.CATEGORY_BLOCK.matcher(pageHtml);
        String categoryBlock = categoryBlockMatcher.find() ? categoryBlockMatcher.group(3) : "";

        return categoryUrlsFromPageBlock(categoryBlock);
    }

    public Set<String> categoryUrlsFromMainPage(String pageHtml) {
        Matcher categoryBlockMatcher = WildberriesPatterns.MAIN_CATEGORY_BLOCK.matcher(pageHtml);
        String categoryBlock = categoryBlockMatcher.find() ? categoryBlockMatcher.group(1) : "";

        return categoryUrlsFromPageBlock(categoryBlock);
    }

    private Set<String> categoryUrlsFromPageBlock(String categoryBlock) {
        Matcher categoryMatcher = WildberriesPatterns.WB_SUBCATEGORY_URL.matcher(categoryBlock);
        Set<String> categories = new HashSet<>();

        while (categoryMatcher.find())
            categories.add("https://www.wildberries.ru" + categoryMatcher.group(1));

        return categories;
    }
}
