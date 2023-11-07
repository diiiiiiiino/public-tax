package com.nos.tax.waterbill.command.domain.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.response.ErrorCode;

/**
 * 수도요금이 계산 가능 상태가 아닐때 발생하는 예외
 */
public class WaterBillNotCalculateStateException extends ApplicationException {
    public WaterBillNotCalculateStateException(String message) {
        super(message, ErrorCode.WATER_BILL_NOT_CALCULATE_STATE);
    }
}
