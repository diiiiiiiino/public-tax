package com.nos.tax.common.exception;

import com.nos.tax.common.http.ErrorCode;
import lombok.Getter;

import java.util.List;

@Getter
public class ValidationErrorException extends ApplicationException{
    private List<ValidationError> errors;

    public ValidationErrorException(String message) {
        super(message, ErrorCode.INVALID_REQUEST);
    }

    public ValidationErrorException(String message, List<ValidationError> errors) {
        super(message, ErrorCode.INVALID_REQUEST);
        this.errors = errors;
    }
}
