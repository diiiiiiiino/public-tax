package com.nos.tax.member.domain;

public class PasswordChangeException extends RuntimeException {
    public PasswordChangeException(){
        super();
    }
    public PasswordChangeException(String message) {
        super(message);
    }
}
