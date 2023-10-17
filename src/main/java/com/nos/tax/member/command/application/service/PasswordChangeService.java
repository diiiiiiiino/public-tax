package com.nos.tax.member.command.application.service;

import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.validator.RequestValidator;
import com.nos.tax.member.command.application.dto.PasswordChangeRequest;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.application.validator.annotation.PasswordChangeRequestQualifier;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 비밀번호 변경 서비스
 */
@Service
public class PasswordChangeService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RequestValidator<PasswordChangeRequest> validator;

    public PasswordChangeService(MemberRepository memberRepository,
                                 PasswordEncoder passwordEncoder,
                                 @PasswordChangeRequestQualifier RequestValidator<PasswordChangeRequest> validator) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    /**
     * 비밀번호 변경
     * @param memberId 회원 ID
     * @param request 비밀번호 변경 요청
     * @throws MemberNotFoundException 회원 미조회
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code orgPassword}가 {@code null}이거나 문자가 없을 경우, 길이가 8~16 아닌 경우
     *     <li>{@code newPassword}가 {@code null}이거나 문자가 없을 경우, 길이가 8~16 아닌 경우
     * </ul>
     */
    @Transactional
    public void change(Long memberId, PasswordChangeRequest request) {
        List<ValidationError> errors = validator.validate(request);
        RequestValidator.validateId(memberId, "memberId", errors);

        if(!errors.isEmpty()){
            throw new ValidationErrorException("Request has invalid values", errors);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        member.changePassword(request.getOrgPassword(), request.getNewPassword(), passwordEncoder);
    }
}
