package com.nos.tax.waterbill.command.domain.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

public class WaterBillNotReadyStateException extends ApplicationException {
    public WaterBillNotReadyStateException(String message) {
        super(message, ErrorCode.WATER_BILL_NOT_READY_STATE);
    }
}
