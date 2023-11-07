package com.nos.tax.waterbill.command.domain.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.response.ErrorCode;

/**
 * 수도요금이 준비상태가 아닐때 발생하는 예외
 */
public class WaterBillNotReadyStateException extends ApplicationException {
    public WaterBillNotReadyStateException(String message) {
        super(message, ErrorCode.WATER_BILL_NOT_READY_STATE);
    }
}
