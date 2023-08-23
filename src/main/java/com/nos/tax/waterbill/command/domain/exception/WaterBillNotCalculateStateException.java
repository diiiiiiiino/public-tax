package com.nos.tax.waterbill.command.domain.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

public class WaterBillNotCalculateStateException extends ApplicationException {
    public WaterBillNotCalculateStateException(String message) {
        super(message, ErrorCode.WATER_BILL_NOT_CALCULATE_STATE);
    }
}
