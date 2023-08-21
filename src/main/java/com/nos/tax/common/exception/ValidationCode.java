package com.nos.tax.common.exception;

import lombok.Getter;

@Getter
public enum ValidationCode {
    NULL("null"),
    EMPTY("empty"),
    NEGATIVE("negative"),
    NO_TEXT("no text"),
    LENGTH("length");

    private String value;

    ValidationCode(String value) {
        this.value = value;
    }
}
