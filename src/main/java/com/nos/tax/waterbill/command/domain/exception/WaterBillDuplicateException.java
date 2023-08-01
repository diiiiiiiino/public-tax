package com.nos.tax.waterbill.command.domain.exception;

public class WaterBillDuplicateException extends RuntimeException {
    public WaterBillDuplicateException(String message) {
        super(message);
    }

    public WaterBillDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
