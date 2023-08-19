package com.nos.tax.util;

import com.nos.tax.common.exception.ValidationException;
import org.springframework.util.StringUtils;

import java.util.Collection;

public class VerifyUtil {
    public static String verifyText(String text){
        if(!StringUtils.hasText(text))
            throw new ValidationException("Has No Text");

        return text;
    }

    public static int verifyNegative(int value){
        if(value < 0){
            throw new ValidationException("no negative");
        }

        return value;
    }

    public static long verifyNegative(long value){
        if(value < 0){
            throw new ValidationException("no negative");
        }

        return value;
    }

    public static void verifyCollection(Collection<?> collection){
        if(collection == null || collection.size() == 0){
            throw new ValidationException("list no element");
        }
    }
}
