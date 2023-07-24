package com.nos.tax.waterbill.domain.exception;

public class WaterBillStateException extends RuntimeException {
    public WaterBillStateException(String message) {
        super(message);
    }

    public WaterBillStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
