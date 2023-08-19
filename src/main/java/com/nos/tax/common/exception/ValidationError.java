package com.nos.tax.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationError {
    private String name;
    private String code;

    public static ValidationError of(String name, String code){
        return new ValidationError(name, code);
    }
}
