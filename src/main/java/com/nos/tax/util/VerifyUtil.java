package com.nos.tax.util;

import com.nos.tax.common.exception.ValidationErrorException;
import org.springframework.util.StringUtils;

import java.util.Collection;

public class VerifyUtil {
    public static String verifyText(String text, String name){
        if(!StringUtils.hasText(text))
            throw new ValidationErrorException(name + " has no text");

        return text;
    }

    public static String verifyTextLength(String text, String name, int min, int max){
        verifyText(text, name);

        int length = text.length();

        if(min > length || max < length){
            throw new ValidationErrorException(name + " is not less than " + min + " and not more than " + max + " digits");
        }

        return text;
    }

    public static int verifyNegative(int value, String name){
        if(value < 0){
            throw new ValidationErrorException(name + " no negative");
        }

        return value;
    }

    public static long verifyNegative(long value, String name){
        if(value < 0){
            throw new ValidationErrorException(name + " no negative");
        }

        return value;
    }

    public static <T> T verifyNull(T obj, String name){
        if(obj == null){
            throw new ValidationErrorException(name + " is null");
        }

        return obj;
    }

    public static void verifyCollection(Collection<?> collection, String name){
        if(collection == null || collection.size() == 0){
            throw new ValidationErrorException(name + " no element");
        }
    }
}
