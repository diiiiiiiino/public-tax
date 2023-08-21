package com.nos.tax.member.command.application.service;

import com.nos.tax.common.component.DateUtils;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.validator.RequestValidator;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.HouseHolder;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.invite.command.domain.MemberInvite;
import com.nos.tax.invite.command.domain.repository.MemberInviteCodeRepository;
import com.nos.tax.member.command.application.dto.MemberCreateRequest;
import com.nos.tax.member.command.application.exception.ExpiredInviteCodeException;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.exception.InviteCodeNotFoundException;
import com.nos.tax.member.command.application.validator.annotation.MemberCreateRequestQualifier;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberCreateService {
    private final DateUtils dateUtils;
    private final MemberInviteCodeRepository memberInviteCodeRepository;
    private final HouseHoldRepository houseHoldRepository;
    private final MemberRepository memberRepository;
    private final RequestValidator validator;

    public MemberCreateService(DateUtils dateUtils,
                               MemberInviteCodeRepository memberInviteCodeRepository,
                               HouseHoldRepository houseHoldRepository,
                               MemberRepository memberRepository,
                               @MemberCreateRequestQualifier RequestValidator validator) {
        this.dateUtils = dateUtils;
        this.memberInviteCodeRepository = memberInviteCodeRepository;
        this.houseHoldRepository = houseHoldRepository;
        this.memberRepository = memberRepository;
        this.validator = validator;
    }

    @Transactional
    public void create(MemberCreateRequest request) {
        List<ValidationError> errors = validator.validate(request);
        if(!errors.isEmpty())
            throw new ValidationErrorException("Request has invalid values", errors);

        MemberInvite memberInvite = memberInviteCodeRepository.findByCode(request.getInviteCode())
                .orElseThrow(() -> new InviteCodeNotFoundException("not found inviteCode"));

        if(memberInvite.isExpired(dateUtils.today())){
            throw new ExpiredInviteCodeException("expired inviteCode");
        }

        HouseHold houseHold = houseHoldRepository.findById(request.getHouseholdId())
                .orElseThrow(() -> new HouseHoldNotFoundException("not found household"));

        Member member = MemberCreateRequest.newMember(request);
        memberRepository.save(member);

        houseHold.moveInHouse(HouseHolder.of(member, member.getName(), member.getMobile()));
    }
}
