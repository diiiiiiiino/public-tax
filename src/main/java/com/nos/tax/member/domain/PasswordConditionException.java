package com.nos.tax.member.domain;

public class PasswordConditionException extends RuntimeException {
    public PasswordConditionException(){
        super();
    }
    public PasswordConditionException(String message) {
        super(message);
    }
}
