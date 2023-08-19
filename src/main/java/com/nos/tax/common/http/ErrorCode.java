package com.nos.tax.common.http;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVITE_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "InviteCodeExpired"),
    INVITE_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "InviteCodeNotFound"),
    HOUSEHOLD_NOT_FOUND(HttpStatus.NOT_FOUND, "HouseHoldNotFound"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MemberNotFound"),
    PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "PasswordNotMatched"),
    UPDATE_PASSWORD_SAME(HttpStatus.BAD_REQUEST, "UpdatePasswordSame"),
    INVALID_VALUE(HttpStatus.BAD_REQUEST, "InvalidValue");

    private HttpStatus status;
    private String code;

    ErrorCode(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }
}
