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

import static com.nos.tax.common.enumeration.TextLengthRange.*;

/**
 * {@code MemberCreateRequest}의 변수의 유효성을 검증하는 클래스
 */
@Validator
@MemberCreateRequestQualifier
public class MemberCreateRequestValidator implements RequestValidator<MemberCreateRequest> {

    /**
     * {@code MemberCreateRequest} 유효성 검증
     * @param request 회원 생성 요청
     * @return List<ValidationError>
     */
    public List<ValidationError> validate(MemberCreateRequest request){
        List<ValidationError> errors = new ArrayList<>();

        if(request == null){
            errors.add(ValidationError.of("request", ValidationCode.EMPTY.getValue()));
        } else {
            String loginId = request.getLoginId();
            String password = request.getPassword();
            String name = request.getName();
            String mobile = request.getMobile();
            String inviteCode = request.getInviteCode();

            if(!StringUtils.hasText(loginId)){
                errors.add(ValidationError.of("memberLoginId", ValidationCode.NO_TEXT.getValue()));
            } else {
                int length = loginId.length();
                if(LOGIN_ID.getMin() > length || LOGIN_ID.getMax() < length)
                    errors.add(ValidationError.of("memberLoginId", ValidationCode.LENGTH.getValue()));
            }

            if(!StringUtils.hasText(password)){
                errors.add(ValidationError.of("memberPassword", ValidationCode.NO_TEXT.getValue()));
            } else {
                int length = password.length();
                if(PASSWORD.getMin() > length || PASSWORD.getMax() < length)
                    errors.add(ValidationError.of("memberPassword", ValidationCode.LENGTH.getValue()));
            }

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
                if(MOBILE.getMin() > length || MOBILE.getMax() < mobile.length())
                    errors.add(ValidationError.of("memberMobile", ValidationCode.LENGTH.getValue()));
            }

            if(request.getHouseholdId() == null){
                errors.add(ValidationError.of("memberHouseHoldId", ValidationCode.EMPTY.getValue()));
            }

            if(!StringUtils.hasText(inviteCode)){
                errors.add(ValidationError.of("memberInviteCode", ValidationCode.NO_TEXT.getValue()));
            } else {
                int length = inviteCode.length();
                if(MEMBER_INVITE_CODE.getMin() > length || MEMBER_INVITE_CODE.getMax() < length)
                    errors.add(ValidationError.of("memberInviteCode", ValidationCode.LENGTH.getValue()));
            }
        }

        return errors;
    }
}
