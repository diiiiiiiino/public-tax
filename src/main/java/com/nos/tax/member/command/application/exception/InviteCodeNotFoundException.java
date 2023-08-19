package com.nos.tax.member.command.application.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

public class InviteCodeNotFoundException extends ApplicationException {
    public InviteCodeNotFoundException(String message) {
        super(message, ErrorCode.INVITE_CODE_NOT_FOUND);
    }
}
