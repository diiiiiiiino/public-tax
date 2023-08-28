package com.nos.tax.common.exception;

import com.nos.tax.common.http.ErrorCode;
import lombok.Getter;

import java.util.Map;

@Getter
public class CustomIllegalArgumentException extends IllegalArgumentException{
    private Map<String, String> detail;
    private ErrorCode errorCode;

    public CustomIllegalArgumentException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomIllegalArgumentException(String message, ErrorCode errorCode, Map<String, String> detail) {
        this(message, errorCode);
        this.detail = detail;
    }
}
