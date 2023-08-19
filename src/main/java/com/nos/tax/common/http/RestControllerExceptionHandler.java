package com.nos.tax.common.http;

import com.nos.tax.common.exception.ApplicationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> totalHandle(ApplicationException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        Response<?> response = Response.builder()
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(errorCode.getStatus())
                .body(response);
    }
}
