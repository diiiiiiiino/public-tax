package com.nos.tax.waterbill.command.domain.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

public class WaterBillDuplicateException extends ApplicationException {
    public WaterBillDuplicateException(String message) {
        super(message, ErrorCode.WATER_BILL_DUPLICATE);
    }

}
