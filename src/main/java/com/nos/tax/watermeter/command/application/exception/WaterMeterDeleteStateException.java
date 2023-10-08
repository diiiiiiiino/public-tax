package com.nos.tax.watermeter.command.application.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

public class WaterMeterDeleteStateException extends ApplicationException {
    public WaterMeterDeleteStateException(String message) {
        super(message, ErrorCode.WATER_METER_DELETE_STATE);
    }
}
