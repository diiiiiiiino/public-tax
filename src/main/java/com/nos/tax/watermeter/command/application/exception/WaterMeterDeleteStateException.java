package com.nos.tax.watermeter.command.application.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.response.ErrorCode;

/**
 * 수도계량 삭제 불가 상태 예외
 */
public class WaterMeterDeleteStateException extends ApplicationException {
    public WaterMeterDeleteStateException(String message) {
        super(message, ErrorCode.WATER_METER_DELETE_STATE);
    }
}
