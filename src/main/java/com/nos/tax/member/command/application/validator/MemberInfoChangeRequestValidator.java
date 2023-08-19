package com.nos.tax.member.command.application.validator;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.member.command.application.dto.MemberInfoChangeRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemberInfoChangeRequestValidator {
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
