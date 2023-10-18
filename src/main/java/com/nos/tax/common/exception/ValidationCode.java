package com.nos.tax.common.exception;

import lombok.Getter;

/**
 * 유효성 검사 시 사용되는 코드
 */
@Getter
public enum ValidationCode {
    /** 데이터가 {@code null}인 경우 */
    NULL("null"),
    /** 문자,배열,컬렉션 등이 비어있는 경우 */
    EMPTY("empty"),
    /** 숫자 데이터가 음수인 경우 */
    NEGATIVE("negative"),
    /** 문자 데이터가 비어있는 경우 */
    NO_TEXT("no text"),
    /** 문자,배열,컬렉션 등이 지정된 길이와 다를 경우*/
    LENGTH("length"),
    /** 체크박스를 하나만 선택해야 하는 경우 */
    SELECT_ONE("selectOne");

    private String value;

    ValidationCode(String value) {
        this.value = value;
    }
}
