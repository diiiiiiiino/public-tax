package com.nos.tax.member.command.application.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

public class HouseHoldNotFoundException extends ApplicationException {
    public HouseHoldNotFoundException(String message) {
        super(message, ErrorCode.HOUSEHOLD_NOT_FOUND);
    }
}
