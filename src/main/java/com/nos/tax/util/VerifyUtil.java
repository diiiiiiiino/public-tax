package com.nos.tax.util;

import org.springframework.util.StringUtils;

public class VerifyUtil {
    public static String verifyText(String text){
        if(!StringUtils.hasText(text))
            throw new IllegalArgumentException("Has No Text");

        return text;
    }
}
