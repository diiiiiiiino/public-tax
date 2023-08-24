package com.nos.tax.watermeter.command.application.validator;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.validator.RequestValidator;
import com.nos.tax.common.validator.Validator;
import com.nos.tax.watermeter.command.application.dto.WaterMeterCreateRequest;

import java.util.ArrayList;
import java.util.List;

@Validator
@WaterMeterCreateRequestQualifier
public class WaterMeterCreateRequestValidator implements RequestValidator<WaterMeterCreateRequest> {

    @Override
    public List<ValidationError> validate(WaterMeterCreateRequest request) {
        List<ValidationError> errors = new ArrayList<>();
        if(request == null){
            errors.add(ValidationError.of("request", ValidationCode.NULL.getValue()));
        } else {

            if(request.getPreviousMeter() < 0){
                errors.add(ValidationError.of("previousMeter", ValidationCode.NEGATIVE.getValue()));
            }

            if(request.getPresentMeter() < 0){
                errors.add(ValidationError.of("presentMeter", ValidationCode.NEGATIVE.getValue()));
            }

            if(request.getCalculateYm() == null){
                errors.add(ValidationError.of("calculateYm", ValidationCode.NULL.getValue()));
            }
        }

        return errors;
    }
}
