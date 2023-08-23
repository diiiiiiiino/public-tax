package com.nos.tax.common.validator;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;

import java.util.List;

public interface RequestValidator<T> {
    List<ValidationError> validate(T request);

    static void validateId(Long id, String name, List<ValidationError> errors){
        if(id == null){
            errors.add(ValidationError.of(name, ValidationCode.NULL.getValue()));
        }
    }
}
