package com.nos.tax.waterbill.command.domain.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

public class WaterMeterNotAllCreatedException extends ApplicationException {
    public WaterMeterNotAllCreatedException(String message) {
        super(message, ErrorCode.WATER_METER_NOT_ALL_CREATED);
    }
}
