package com.nos.tax.watermeter.command.application.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.response.ErrorCode;

/**
 * 수도계량 미조회 예외
 */
public class WaterMeterNotFoundException extends ApplicationException {
    public WaterMeterNotFoundException(String message) {
        super(message, ErrorCode.WATER_METER_NOT_FOUND);
    }
}
