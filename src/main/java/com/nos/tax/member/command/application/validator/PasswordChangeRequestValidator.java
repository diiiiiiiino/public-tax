package com.nos.tax.member.command.application.validator;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.validator.RequestValidator;
import com.nos.tax.common.validator.Validator;
import com.nos.tax.member.command.application.dto.PasswordChangeRequest;
import com.nos.tax.member.command.application.validator.annotation.PasswordChangeRequestQualifier;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.nos.tax.common.enumeration.TextLengthRange.PASSWORD;

/**
 * {@code PasswordChangeRequest}의 변수의 유효성을 검증하는 클래스
 */
@Validator
@PasswordChangeRequestQualifier
public class PasswordChangeRequestValidator implements RequestValidator<PasswordChangeRequest> {

    /**
     * {@code PasswordChangeRequest} 유효성 검증
     * @param request 비밀번호 변경요청
     * @return List<ValidationError>
     */
    public List<ValidationError> validate(PasswordChangeRequest request){
        List<ValidationError> errors = new ArrayList<>();
        if(request == null){
            errors.add(ValidationError.of("request", ValidationCode.NULL.getValue()));
        } else {
            String orgPassword = request.getOrgPassword();
            String newPassword = request.getNewPassword();

            if(!StringUtils.hasText(orgPassword)){
                errors.add(ValidationError.of("memberOrgPassword", ValidationCode.NO_TEXT.getValue()));
            } else {
                int length = orgPassword.length();
                if(PASSWORD.getMin() > length || PASSWORD.getMax() < length){
                    errors.add(ValidationError.of("memberOrgPassword", ValidationCode.LENGTH.getValue()));
                }
            }

            if(!StringUtils.hasText(newPassword)){
                errors.add(ValidationError.of("memberNewPassword", ValidationCode.NO_TEXT.getValue()));
            } else {
                int length = newPassword.length();
                if(PASSWORD.getMin() > length || PASSWORD.getMax() < length){
                    errors.add(ValidationError.of("memberNewPassword", ValidationCode.LENGTH.getValue()));
                }
            }
        }

        return errors;
    }
}
