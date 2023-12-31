package com.nos.tax.waterbill.command.application.validator;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.validator.RequestValidator;
import com.nos.tax.common.validator.Validator;
import com.nos.tax.waterbill.command.application.dto.WaterBillCreateRequest;
import com.nos.tax.waterbill.command.application.validator.annotation.WaterBillCreateRequestQualifier;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code WaterBillCreateRequest}의 변수의 유효성을 검증하는 클래스
 */
@Validator
@WaterBillCreateRequestQualifier
public class WaterBillCreateRequestValidator implements RequestValidator<WaterBillCreateRequest> {
    public List<ValidationError> validate(WaterBillCreateRequest request){
        List<ValidationError> errors = new ArrayList<>();

        if(request == null){
            errors.add(ValidationError.of("request", ValidationCode.EMPTY.getValue()));
        } else {
            Integer totalAmount = request.getTotalAmount();
            YearMonth calculateYm = request.getCalculateYm();

            if(totalAmount == null){
                errors.add(ValidationError.of("totalAmount", ValidationCode.EMPTY.getValue()));
            } else {
                if(totalAmount < 0){
                    errors.add(ValidationError.of("totalAmount", ValidationCode.NEGATIVE.getValue()));
                }
            }

            if(calculateYm == null){
                errors.add(ValidationError.of("calculateYm", ValidationCode.NULL.getValue()));
            }
        }

        return errors;
    }
}
