package com.nos.tax.util;

import com.nos.tax.common.exception.CustomIllegalArgumentException;
import com.nos.tax.common.exception.CustomNullPointerException;
import com.nos.tax.common.http.ErrorCode;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 유효성 Util 클래스
 */
public class VerifyUtil {

    /**
     * 문자 유효성 검사
     * @param text 검사 문자
     * @param name 데이터 이름
     * @return String
     * @throws CustomIllegalArgumentException {@code text}가 {@code null}이거나 빈 문자열일 때
     */
    public static String verifyText(String text, String name){
        if(!StringUtils.hasText(text)){
            throw new CustomIllegalArgumentException(name + " has no text", ErrorCode.NO_TEXT, Map.of("name", name));
        }

        return text;
    }

    /**
     * 문자 길이 검사
     * @param text 검사 문자
     * @param name 데이터 이름
     * @param min 최소 길이
     * @param max 최대 길이
     * @return String
     * @throws CustomIllegalArgumentException 
     * <ul>
     *     <li>{@code text}가 {@code null}이거나 빈 문자열일 때</li>
     *     <li>지정된 길이에 맞지 않을때</li>
     * </ul>
     */
    public static String verifyTextLength(String text, String name, int min, int max){
        verifyText(text, name);

        int length = text.length();

        if(min > length || max < length){
            throw new CustomIllegalArgumentException(name + " is not less than " + min + " and not more than " + max + " digits", ErrorCode.NOT_MATCH_LENGTH, Map.of("name", name));
        }

        return text;
    }

    /**
     * {@code int} 데이터가 음수인지 검증
     * @param value 정수값
     * @param name 데이터 이름
     * @return int
     * @throws CustomIllegalArgumentException 음수일때
     */
    public static int verifyNegative(int value, String name){
        if(value < 0){
            throw new CustomIllegalArgumentException(name + " no negative", ErrorCode.NEGATIVE_NUMBER, Map.of("name", name));
        }

        return value;
    }

    /**
     * {@code long} 데이터가 음수인지 검증
     * @param value 정수값
     * @param name 데이터 이름
     * @return long
     * @throws CustomIllegalArgumentException 음수일때
     */
    public static long verifyNegative(long value, String name){
        if(value < 0){
            throw new CustomIllegalArgumentException(name + " no negative", ErrorCode.NEGATIVE_NUMBER, Map.of("name", name));
        }

        return value;
    }

    /**
     * 객체의 {@code null} 검사
     * @param obj 객체
     * @param name 객체의 이름
     * @param <T> 검증할 객체 타입
     * @return obj
     * @throws CustomNullPointerException {@code obj}가 {@code null}일 때
     */
    public static <T> T verifyNull(T obj, String name){
        if(obj == null){
            throw new CustomNullPointerException(name + " is null", Map.of("name", name));
        }

        return obj;
    }

    /**
     * 컬렉션이 비어있거나 {@code null}을 검증
     * @param collection 컬렉션
     * @param name 컬렉션 이름
     * @throws CustomIllegalArgumentException 컬렉션이 비어있거나 {@code null}일 때
     */
    public static void verifyCollection(Collection<?> collection, String name){
        if(collection == null || collection.size() == 0){
            throw new CustomIllegalArgumentException(name + " no element", ErrorCode.ILLEGAL_COLLECTION, Map.of("name", name));
        }
    }
}
