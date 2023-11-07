package com.nos.tax.waterbill.command.domain.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.response.ErrorCode;

/**
 * 수도 계량 데이터가 전부 생성되지 않았을때 발생하는 예외
 */
public class WaterMeterNotAllCreatedException extends ApplicationException {
    public WaterMeterNotAllCreatedException(String message) {
        super(message, ErrorCode.WATER_METER_NOT_ALL_CREATED);
    }
}
