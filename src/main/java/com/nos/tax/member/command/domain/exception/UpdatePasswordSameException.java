package com.nos.tax.member.command.domain.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

public class UpdatePasswordSameException extends ApplicationException {
    public UpdatePasswordSameException(String message) {
        super(message, ErrorCode.UPDATE_PASSWORD_SAME);
    }
}
