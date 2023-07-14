package com.nos.tax.member.domain.exception;

public class PasswordConditionException extends RuntimeException {
    public PasswordConditionException(){
        super();
    }
    public PasswordConditionException(String message) {
        super(message);
    }
}
