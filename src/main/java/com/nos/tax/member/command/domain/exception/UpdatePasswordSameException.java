package com.nos.tax.member.command.domain.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.response.ErrorCode;

/**
 * 기존 비밀번호화 변경할 비밀번호가 동일한 예외
 */
public class UpdatePasswordSameException extends ApplicationException {
    public UpdatePasswordSameException(String message) {
        super(message, ErrorCode.UPDATE_PASSWORD_SAME);
    }
}
