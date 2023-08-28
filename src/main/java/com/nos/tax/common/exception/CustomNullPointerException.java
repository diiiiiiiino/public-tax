package com.nos.tax.common.exception;

import com.nos.tax.common.http.ErrorCode;
import lombok.Getter;

import java.util.Map;

@Getter
public class CustomNullPointerException extends NullPointerException{
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
