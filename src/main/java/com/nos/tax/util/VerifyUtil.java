package com.nos.tax.util;

import org.springframework.util.StringUtils;

import java.util.List;

public class VerifyUtil {
    public static String verifyText(String text){
        if(!StringUtils.hasText(text))
            throw new IllegalArgumentException("Has No Text");

        return text;
    }

    public static int verifyNegative(int value){
        if(value < 0){
            throw new IllegalArgumentException("no negative");
        }

        return value;
    }

    public static long verifyNegative(long value){
        if(value < 0){
            throw new IllegalArgumentException("no negative");
        }

        return value;
    }

    public static void verifyList(List<?> list){
        if(list == null || list.size() == 0){
            throw new IllegalArgumentException("list no element");
        }
    }
}
