package com.nos.tax.common.exception;

import com.nos.tax.common.http.ErrorCode;
import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException {
    protected ErrorCode errorCode;

    public ApplicationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
