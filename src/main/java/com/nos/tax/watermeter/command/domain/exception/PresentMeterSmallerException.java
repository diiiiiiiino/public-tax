package com.nos.tax.watermeter.command.domain.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.response.ErrorCode;

import java.util.Map;

/**
 * 이번달 수도계량이 저번달 수도계량보다 작았을 때 발생하는 예외 
 */
public class PresentMeterSmallerException extends ApplicationException {
    public PresentMeterSmallerException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public PresentMeterSmallerException(String message, ErrorCode errorCode, Map<String, String> detail) {
        super(message, errorCode, detail);
    }
}
