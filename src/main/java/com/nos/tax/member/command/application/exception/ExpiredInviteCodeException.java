package com.nos.tax.member.command.application.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

/**
 * 초대코드 만료 예외
 */
public class ExpiredInviteCodeException extends ApplicationException {
    public ExpiredInviteCodeException(String message) {
        super(message, ErrorCode.INVITE_CODE_EXPIRED);
    }
}
