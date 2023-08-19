package com.nos.tax.common.http;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.exception.ValidationErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> totalHandle(ApplicationException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        Response.ResponseBuilder builder = Response.builder();

        if(exception instanceof ValidationErrorException){
            ValidationErrorException validationErrorException = (ValidationErrorException) exception;
            builder.errors(validationErrorException.getErrors());
        }

        Response<?> response = builder.message(exception.getMessage())
                .errorCode(errorCode.getCode())
                .build();

        return ResponseEntity.status(errorCode.getStatus())
                .body(response);
    }
}
