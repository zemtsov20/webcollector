package com.contentparser.beans;

import com.common.entity.ParsedContent;
import com.common.entity.ParsedContentTs;
import com.common.enums.State;
import com.common.patterns.wb.WildberriesPatterns;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;

@Component
public class GetContentFromHtml {
    public Optional<ParsedContentTs> contentTsFromPage(ParsedContent parsedContent) {
        String pageHtml = parsedContent.getHtml();
        if(pageHtml == null)
            return Optional.empty();

        Matcher idMatcher = WildberriesPatterns.PRODUCT_ID.matcher(pageHtml);
        Matcher nameMatcher = WildberriesPatterns.PRODUCT_NAME.matcher(pageHtml);
        Matcher priceMatcher = WildberriesPatterns.PRODUCT_PRICE.matcher(pageHtml);

        Long id = idMatcher.find() ? Long.parseLong(idMatcher.group(1)) : -1;
        String name = nameMatcher.find() ? nameMatcher.group(1) : "no name";

        StringBuilder strPrice = new StringBuilder();
        int i = 1;
        if(priceMatcher.find()) {
            while (i < priceMatcher.groupCount()) {
                strPrice.append(priceMatcher.group(i++));
            }
        }
        Integer price = strPrice.length() > 0 ? Integer.parseInt(strPrice.toString()) : -1;

        ParsedContentTs parsedContentTs = new ParsedContentTs(id, price);

        return Optional.of(parsedContentTs);
    }

    public ParsedContent contentFromPage(ParsedContent parsedContent) {
        String pageHtml = parsedContent.getHtml();
        if(pageHtml == null)
            return parsedContent;

        Matcher idMatcher = WildberriesPatterns.PRODUCT_ID.matcher(pageHtml);
        Matcher nameMatcher = WildberriesPatterns.PRODUCT_NAME.matcher(pageHtml);

        Long id = idMatcher.find() ? Long.parseLong(idMatcher.group(1)) : -1;
        String name = nameMatcher.find() ? nameMatcher.group(1) : "no name";

        parsedContent.setProductId(id);
        parsedContent.setProductName(name);
        parsedContent.setState(State.PARSED);

        return parsedContent;
    }
}
