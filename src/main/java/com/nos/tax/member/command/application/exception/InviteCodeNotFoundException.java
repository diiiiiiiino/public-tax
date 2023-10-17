package com.nos.tax.member.command.application.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

/**
 * 초대코드 미조회 예외
 */
public class InviteCodeNotFoundException extends ApplicationException {
    public InviteCodeNotFoundException(String message) {
        super(message, ErrorCode.INVITE_CODE_NOT_FOUND);
    }
}
