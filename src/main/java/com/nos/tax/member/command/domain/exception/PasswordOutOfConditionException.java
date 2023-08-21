package com.nos.tax.member.command.domain.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

public class PasswordOutOfConditionException extends ApplicationException {
    public PasswordOutOfConditionException(String message) {
        super(message, ErrorCode.PASSWORD_OUT_OF_CONDITION);
    }
}
