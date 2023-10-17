package com.nos.tax.authority.command.application.exception;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.ErrorCode;

/**
 * 권한 미조회 예외
 */
public class AuthorityNotFoundException extends ApplicationException {
    public AuthorityNotFoundException(String message) {
        super(message, ErrorCode.AUTHORITY_NOT_FOUND);
    }
}
