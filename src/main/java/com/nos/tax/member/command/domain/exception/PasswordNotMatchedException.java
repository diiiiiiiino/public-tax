package com.nos.tax.member.command.domain.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

public class PasswordNotMatchedException extends ApplicationException {
    public PasswordNotMatchedException(String message) {
        super(message, ErrorCode.PASSWORD_NOT_MATCHED);
    }
}
