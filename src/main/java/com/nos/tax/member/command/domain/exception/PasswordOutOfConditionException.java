package com.nos.tax.member.command.domain.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.response.ErrorCode;

/**
 * 비밀번호 정책 위반 예외
 */
public class PasswordOutOfConditionException extends ApplicationException {
    public PasswordOutOfConditionException(String message) {
        super(message, ErrorCode.PASSWORD_OUT_OF_CONDITION);
    }
}
