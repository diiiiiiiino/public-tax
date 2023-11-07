package com.nos.tax.member.command.application.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.response.ErrorCode;

/**
 * 세대 미조회 예외
 */
public class HouseHoldNotFoundException extends ApplicationException {
    public HouseHoldNotFoundException(String message) {
        super(message, ErrorCode.HOUSEHOLD_NOT_FOUND);
    }
}
