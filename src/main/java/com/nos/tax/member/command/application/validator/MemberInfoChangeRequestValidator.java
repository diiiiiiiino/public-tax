package com.nos.tax.member.command.application.validator;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.validator.RequestValidator;
import com.nos.tax.common.validator.Validator;
import com.nos.tax.member.command.application.dto.MemberInfoChangeRequest;
import com.nos.tax.member.command.application.validator.annotation.MemberInfoChangeRequestQualifier;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Validator
@MemberInfoChangeRequestQualifier
public class MemberInfoChangeRequestValidator implements RequestValidator<MemberInfoChangeRequest> {
    public List<ValidationError> validate(MemberInfoChangeRequest request){
        List<ValidationError> errors = new ArrayList<>();
        if(request == null){
            errors.add(ValidationError.of("request", ValidationCode.NULL.getValue()));
        } else{
            if(!StringUtils.hasText(request.getName()))
                errors.add(ValidationError.of("memberName", ValidationCode.NO_TEXT.getValue()));
            if(!StringUtils.hasText(request.getMobile()))
                errors.add(ValidationError.of("memberMobile", ValidationCode.NO_TEXT.getValue()));
        }

        return errors;
    }
}
