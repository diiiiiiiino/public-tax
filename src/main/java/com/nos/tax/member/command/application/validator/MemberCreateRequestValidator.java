package com.nos.tax.member.command.application.validator;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.validator.RequestValidator;
import com.nos.tax.common.validator.Validator;
import com.nos.tax.member.command.application.dto.MemberCreateRequest;
import com.nos.tax.member.command.application.validator.annotation.MemberCreateRequestQualifier;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Validator
@MemberCreateRequestQualifier
public class MemberCreateRequestValidator implements RequestValidator<MemberCreateRequest> {
    public List<ValidationError> validate(MemberCreateRequest request){
        List<ValidationError> errors = new ArrayList<>();

        if(request == null){
            errors.add(ValidationError.of("request", ValidationCode.EMPTY.getValue()));
        } else {
            if(!StringUtils.hasText(request.getLoginId()))
                errors.add(ValidationError.of("memberLoginId", ValidationCode.NO_TEXT.getValue()));
            if(!StringUtils.hasText(request.getPassword()))
                errors.add(ValidationError.of("memberPassword", ValidationCode.NO_TEXT.getValue()));
            if(!StringUtils.hasText(request.getName()))
                errors.add(ValidationError.of("memberName", ValidationCode.NO_TEXT.getValue()));
            if(!StringUtils.hasText(request.getMobile()))
                errors.add(ValidationError.of("memberMobile", ValidationCode.NO_TEXT.getValue()));
            if(request.getHouseholdId() == null)
                errors.add(ValidationError.of("memberHouseHoldId", ValidationCode.EMPTY.getValue()));
            if(!StringUtils.hasText(request.getInviteCode()))
                errors.add(ValidationError.of("memberInviteCode", ValidationCode.NO_TEXT.getValue()));
        }

        return errors;
    }
}
