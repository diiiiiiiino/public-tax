package com.nos.tax.member.command.application.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

public class ExpiredInviteCodeException extends ApplicationException {
    public ExpiredInviteCodeException(String message) {
        super(message, ErrorCode.INVITE_CODE_EXPIRED);
    }
}
