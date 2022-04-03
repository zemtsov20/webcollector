package com.common.patterns.wb;

import java.util.regex.Pattern;

public class WildberriesPatterns {
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

    public static final Pattern WB_PRODUCT_URL
            = Pattern.compile("((?:http|https)://www\\.wildberries\\.ru/catalog/[\\d]*/detail.aspx)|" +
                                    "(/catalog/[\\d]*/detail.aspx)",
            Pattern.CASE_INSENSITIVE);

    public static final Pattern CATEGORY_BLOCK
            = Pattern.compile("(<div class=\"menu-catalog\">|hasnochild)([\\s\\S]*?)li([\\s\\S]*?)</div>",
            //= Pattern.compile("(<div class=\"menu-catalog\">|hasnochild)([\\s\\S]*?)li([\\s\\S]*?)</div>",
            Pattern.CASE_INSENSITIVE);

    public static final Pattern WB_SUBCATEGORY_URL
            = Pattern.compile("(/catalog/[/\\w-]*)\"",
            Pattern.CASE_INSENSITIVE);

    public static final Pattern MAIN_CATEGORY_BLOCK = Pattern.compile("menu-burger__main([\\s\\S]*?)<div", Pattern.CASE_INSENSITIVE);
}
