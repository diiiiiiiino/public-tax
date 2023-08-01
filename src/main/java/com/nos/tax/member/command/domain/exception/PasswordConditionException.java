package com.nos.tax.member.command.domain.exception;

public class PasswordConditionException extends RuntimeException {
    public PasswordConditionException(){
        super();
    }
    public PasswordConditionException(String message) {
        super(message);
    }
}
