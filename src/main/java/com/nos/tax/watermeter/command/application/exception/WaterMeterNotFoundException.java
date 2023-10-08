package com.nos.tax.watermeter.command.application.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

public class WaterMeterNotFoundException extends ApplicationException {
    public WaterMeterNotFoundException(String message) {
        super(message, ErrorCode.WATER_METER_NOT_FOUND);
    }
}
