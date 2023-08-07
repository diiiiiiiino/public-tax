package com.nos.tax.member.command.application.exception;

public class ExpiredInviteCodeException extends RuntimeException {
    public ExpiredInviteCodeException(String message) {
        super(message);
    }

    public ExpiredInviteCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
