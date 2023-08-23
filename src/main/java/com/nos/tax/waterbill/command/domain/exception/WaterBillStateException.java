package com.nos.tax.waterbill.command.domain.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

public class WaterBillStateException extends ApplicationException {
    public WaterBillStateException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
