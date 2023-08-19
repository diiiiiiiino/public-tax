package com.nos.tax.common.exception;

import com.nos.tax.common.http.ErrorCode;

public class ValidationException extends ApplicationException{
    public ValidationException(String message) {
        super(message, ErrorCode.INVALID_VALUE);
    }
}
