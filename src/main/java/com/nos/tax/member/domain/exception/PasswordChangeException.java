package com.nos.tax.member.domain.exception;

public class PasswordChangeException extends RuntimeException {
    public PasswordChangeException(){
        super();
    }
    public PasswordChangeException(String message) {
        super(message);
    }
}
