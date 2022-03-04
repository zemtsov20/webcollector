package com.common.patterns;

import java.util.regex.Pattern;

public class Patterns {
    public static final Pattern FIRST_AND_SECOND_DOMAIN
            = Pattern.compile("(?:https://|http://|//)?" +
                    "(?:www.)?[-a-zA-Z0-9@:%._+~#=]{1,128}\\.([a-zA-Z0-9()]{1,64}\\.[a-zA-Z0-9()]{1,64})",
            Pattern.CASE_INSENSITIVE);
    // https://[^/]*?(\w+\.\w+)/

    public static final Pattern EMAIL_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    // gr 1 - tag, gr 2 - url, gr 3 - protocol
    public static final Pattern WEB_URL2
            = Pattern.compile("<(a|link|area)\\W?[^>]*\\W+href\\W*=\\W*[\"']((https://|http://|//|/)+[^\"']+)[\"'][^>]*>");

    public static final Pattern PRODUCT_ID
            = Pattern.compile("id=\"productNmId\"[^\\d]*(\\d*)",
                                Pattern.CASE_INSENSITIVE);

    public static final Pattern PRODUCT_NAME
            = Pattern.compile("data-link=\"text\\{:product\\^goodsName}\">(.*)</span>",
                                Pattern.CASE_INSENSITIVE);

    // gr 1 - millions if exists, gr 2 - thousands if exists, gr 3 - units
    public static final Pattern PRODUCT_PRICE
            = Pattern.compile("class=\"price-block__final-price\">\\D*(\\d{0,3})[^;]*;(\\d{0,3})[^;]*;(\\d{0,3})",
                                Pattern.CASE_INSENSITIVE);
}