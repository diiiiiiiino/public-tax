package com.nos.tax.common.exception;

import com.nos.tax.common.http.ErrorCode;
import lombok.Getter;

import java.util.Map;

/**
 * {@code NullPointerException}를 상속한 예외
 */
@Getter
public class CustomNullPointerException extends NullPointerException{
    /**예외 원인의 상세 정보*/
    private Map<String, String> detail;
    private ErrorCode errorCode;

    public CustomNullPointerException(String message) {
        super(message);
        this.errorCode = ErrorCode.NULL;
    }

    public CustomNullPointerException(String message, Map<String, String> detail) {
        this(message);
        this.detail = detail;
    }
}
