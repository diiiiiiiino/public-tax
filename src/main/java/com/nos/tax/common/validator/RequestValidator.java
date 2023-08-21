package com.nos.tax.common.validator;

import com.nos.tax.common.exception.ValidationError;

import java.util.List;

public interface RequestValidator<T> {
    List<ValidationError> validate(T request);
}
