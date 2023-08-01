package com.nos.tax.waterbill.command.domain.exception;

public class WaterBillStateException extends RuntimeException {
    public WaterBillStateException(String message) {
        super(message);
    }

    public WaterBillStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
