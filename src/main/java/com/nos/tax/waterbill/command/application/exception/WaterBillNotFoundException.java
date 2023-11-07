package com.nos.tax.waterbill.command.application.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.response.ErrorCode;

/**
 * 수도요금 미조회 예외
 */
public class WaterBillNotFoundException extends ApplicationException {
    public WaterBillNotFoundException(String message) {
        super(message, ErrorCode.WATER_BILL_NOT_FOUND);
    }
}
