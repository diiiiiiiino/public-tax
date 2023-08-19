package com.nos.tax.member.command.application.validator;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.member.command.application.dto.PasswordChangeRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class PasswordChangeRequestValidator {
    public List<ValidationError> validate(PasswordChangeRequest request){
        List<ValidationError> errors = new ArrayList<>();
        if(request == null){
            errors.add(ValidationError.of("request", ValidationCode.NULL.getValue()));
        } else{
            if(!StringUtils.hasText(request.getOrgPassword()))
                errors.add(ValidationError.of("memberOrgPassword", ValidationCode.NO_TEXT.getValue()));
            if(!StringUtils.hasText(request.getNewPassword()))
                errors.add(ValidationError.of("memberNewPassword", ValidationCode.NO_TEXT.getValue()));
        }

        return errors;
    }
}