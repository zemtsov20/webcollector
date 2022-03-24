package com.contentparser.beans;

import com.common.entity.ParsedContent;
import com.common.patterns.wb.WildberriesPatterns;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;

@Component
public class GetContentFromHtml {
//    public Set<String> emailsFromPage(String pageHtml) {
//        if(pageHtml == null)
//            return null;
//        Set<String> newEmails = new HashSet<>();
//        Matcher emailMatcher = Patterns.EMAIL_ADDRESS.matcher(pageHtml);
//        while (emailMatcher.find()) {
//            newEmails.add(emailMatcher.group());
//        }
//
//        return newEmails;
//    }

    public Optional<ParsedContent> productFromPage(String pageHtml) {
        if(pageHtml == null)
            return Optional.empty();

        Matcher idMatcher = WildberriesPatterns.PRODUCT_ID.matcher(pageHtml);
        Matcher nameMatcher = WildberriesPatterns.PRODUCT_NAME.matcher(pageHtml);
        Matcher priceMatcher = WildberriesPatterns.PRODUCT_PRICE.matcher(pageHtml);

        Integer id = idMatcher.find() ? Integer.parseInt(idMatcher.group(1)) : -1;
        String name = nameMatcher.find() ? nameMatcher.group(1) : "no name";

        StringBuilder strPrice = new StringBuilder();
        int i = 1;
        if(priceMatcher.find()) {
            while (i < priceMatcher.groupCount()) {
                strPrice.append(priceMatcher.group(i++));
            }
        }
        Integer price = strPrice.length() > 0 ? Integer.parseInt(strPrice.toString()) : -1;

        ParsedContent parsedContent = new ParsedContent(id, name, price);

        return Optional.of(parsedContent);
    }


}
