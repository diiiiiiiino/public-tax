package com.nos.tax.common.exception;

import com.nos.tax.common.http.ErrorCode;
import lombok.Getter;

import java.util.Map;

/**
 * 애플리케이션에서 발생한 예외를 나타낸 예외
 */
@Getter
public abstract class ApplicationException extends RuntimeException {
    protected ErrorCode errorCode;
    protected Map<String, String> detail;

    public ApplicationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApplicationException(String message, ErrorCode errorCode, Map<String, String> detail) {
        super(message);
        this.errorCode = errorCode;
        this.detail = detail;
    }
}
