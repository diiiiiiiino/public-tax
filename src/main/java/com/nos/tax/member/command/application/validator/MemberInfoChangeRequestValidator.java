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

import static com.nos.tax.common.enumeration.TextLengthRange.MEMBER_NAME;
import static com.nos.tax.common.enumeration.TextLengthRange.MOBILE;

/**
 * {@code MemberInfoChangeRequest}의 변수의 유효성을 검증하는 클래스
 */
@Validator
@MemberInfoChangeRequestQualifier
public class MemberInfoChangeRequestValidator implements RequestValidator<MemberInfoChangeRequest> {

    /**
     * {@code MemberInfoChangeRequest} 유효성 검증
     * @param request 회원정보 변경 요청
     * @return List<ValidationError>
     */
    public List<ValidationError> validate(MemberInfoChangeRequest request){
        List<ValidationError> errors = new ArrayList<>();
        if(request == null){
            errors.add(ValidationError.of("request", ValidationCode.NULL.getValue()));
        } else {
            String name = request.getName();
            String mobile = request.getMobile();

            if(!StringUtils.hasText(name)){
                errors.add(ValidationError.of("memberName", ValidationCode.NO_TEXT.getValue()));
            } else {
                int length = name.length();
                if(MEMBER_NAME.getMin() > length || MEMBER_NAME.getMax() < length)
                    errors.add(ValidationError.of("memberName", ValidationCode.LENGTH.getValue()));
            }

            if(!StringUtils.hasText(mobile)){
                errors.add(ValidationError.of("memberMobile", ValidationCode.NO_TEXT.getValue()));
            } else {
                int length = mobile.length();
                if(MOBILE.getMin() > length || MOBILE.getMax() < length)
                    errors.add(ValidationError.of("memberMobile", ValidationCode.LENGTH.getValue()));
            }
        }

        return errors;
    }
}
