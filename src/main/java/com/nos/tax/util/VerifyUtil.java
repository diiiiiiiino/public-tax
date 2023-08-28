package com.nos.tax.util;

import com.nos.tax.common.exception.CustomIllegalArgumentException;
import com.nos.tax.common.exception.CustomNullPointerException;
import com.nos.tax.common.http.ErrorCode;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

public class VerifyUtil {
    public static String verifyText(String text, String name){
        if(!StringUtils.hasText(text)){
            throw new CustomIllegalArgumentException(name + " has no text", ErrorCode.NO_TEXT, Map.of("name", name));
        }

        return text;
    }

    public static String verifyTextLength(String text, String name, int min, int max){
        verifyText(text, name);

        int length = text.length();

        if(min > length || max < length){
            throw new CustomIllegalArgumentException(name + " is not less than " + min + " and not more than " + max + " digits", ErrorCode.NOT_MATCH_LENGTH, Map.of("name", name));
        }

        return text;
    }

    public static int verifyNegative(int value, String name){
        if(value < 0){
            throw new CustomIllegalArgumentException(name + " no negative", ErrorCode.NEGATIVE_NUMBER, Map.of("name", name));
        }

        return value;
    }

    public static long verifyNegative(long value, String name){
        if(value < 0){
            throw new CustomIllegalArgumentException(name + " no negative", ErrorCode.NEGATIVE_NUMBER, Map.of("name", name));
        }

        return value;
    }

    public static <T> T verifyNull(T obj, String name){
        if(obj == null){
            throw new CustomNullPointerException(name + " is null", Map.of("name", name));
        }

        return obj;
    }

    public static void verifyCollection(Collection<?> collection, String name){
        if(collection == null || collection.size() == 0){
            throw new CustomIllegalArgumentException(name + " no element", ErrorCode.ILLEGAL_COLLECTION, Map.of("name", name));
        }
    }
}
