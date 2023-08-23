package com.nos.tax.waterbill.command.application.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

public class WaterBillNotFoundException extends ApplicationException {
    public WaterBillNotFoundException(String message) {
        super(message, ErrorCode.WATER_BILL_NOT_FOUND);
    }
}
