package com.nos.tax.watermeter.command.domain.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

import java.util.Map;

public class PresentMeterSmallerException extends ApplicationException {
    public PresentMeterSmallerException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public PresentMeterSmallerException(String message, ErrorCode errorCode, Map<String, String> detail) {
        super(message, errorCode, detail);
    }
}
