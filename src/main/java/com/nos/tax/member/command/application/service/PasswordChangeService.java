package com.nos.tax.member.command.application.service;

import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.validator.RequestValidator;
import com.nos.tax.member.command.application.dto.PasswordChangeRequest;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.application.validator.annotation.PasswordChangeRequestQualifier;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PasswordChangeService {

    private final MemberRepository memberRepository;
    private final RequestValidator validator;

    public PasswordChangeService(MemberRepository memberRepository,
                                 @PasswordChangeRequestQualifier RequestValidator validator) {
        this.memberRepository = memberRepository;
        this.validator = validator;
    }

    @Transactional
    public void change(Member member, PasswordChangeRequest request) {
        List<ValidationError> errors = validator.validate(request);
        if(!errors.isEmpty()){
            throw new ValidationErrorException("Request has invalid values", errors);
        }

        member = memberRepository.findById(member.getId())
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        member.changePassword(request.getOrgPassword(), request.getNewPassword());
    }
}
