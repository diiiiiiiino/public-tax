package com.nos.tax.waterbill.command.domain.exception;

public class WaterBillCalculateConditionException extends RuntimeException {
    public WaterBillCalculateConditionException(String message) {
        super(message);
    }

    public WaterBillCalculateConditionException(String message, Throwable cause) {
        super(message, cause);
    }
}