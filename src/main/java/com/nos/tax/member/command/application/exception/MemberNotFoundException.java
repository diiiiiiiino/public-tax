package com.nos.tax.member.command.application.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

public class MemberNotFoundException extends ApplicationException {
    public MemberNotFoundException(String message) {
        super(message, ErrorCode.MEMBER_NOT_FOUND);
    }
}
