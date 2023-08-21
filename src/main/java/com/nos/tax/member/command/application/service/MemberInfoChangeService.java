package com.nos.tax.member.command.application.service;

import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.validator.RequestValidator;
import com.nos.tax.member.command.application.dto.MemberInfoChangeRequest;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.application.validator.annotation.MemberInfoChangeRequestQualifier;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberInfoChangeService {

    private final MemberRepository memberRepository;
    private final RequestValidator validator;

    public MemberInfoChangeService(MemberRepository memberRepository,
                                   @MemberInfoChangeRequestQualifier RequestValidator validator) {
        this.memberRepository = memberRepository;
        this.validator = validator;
    }

    @Transactional
    public void change(Member member, MemberInfoChangeRequest request) {
        List<ValidationError> errors = validator.validate(request);
        if(!errors.isEmpty()){
            throw new ValidationErrorException("Request has invalid values", errors);
        }

        member = memberRepository.findById(member.getId())
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        member.changeName(request.getName());
        member.changeMobile(Mobile.of(request.getMobile()));
    }
}
