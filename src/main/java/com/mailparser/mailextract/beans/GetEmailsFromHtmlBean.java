package com.mailparser.mailextract.beans;


import com.mailparser.mailextract.enums.HtmlState;
import com.mailparser.mailextract.patterns.Patterns;
import com.mailparser.mailextract.repository.UrlDataEntityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

@Component
public class GetEmailsFromHtmlBean {
    @Autowired
    private UrlDataEntityRepository urlDataEntityRepository;

    public Set<String> emailsFromPage(String pageHtml) {
        if(pageHtml == null)
            return null;
        Set<String> newEmails = new HashSet<>();
        Matcher emailMatcher = Patterns.EMAIL_ADDRESS.matcher(pageHtml);
        while (emailMatcher.find()) {
            newEmails.add(emailMatcher.group());
        }
        return newEmails;
    }
}
