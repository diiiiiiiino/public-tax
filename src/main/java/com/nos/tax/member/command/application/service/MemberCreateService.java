package com.nos.tax.member.command.application.service;

import com.nos.tax.application.component.DateUtils;
import com.nos.tax.application.exception.NotFoundException;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.HouseHolder;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.invite.command.domain.MemberInvite;
import com.nos.tax.invite.command.domain.repository.MemberInviteCodeRepository;
import com.nos.tax.member.command.application.dto.MemberCreateRequest;
import com.nos.tax.member.command.application.exception.ExpiredInviteCodeException;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.member.command.domain.Password;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import com.nos.tax.util.VerifyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberCreateService {
    private final DateUtils dateUtils;
    private final MemberInviteCodeRepository memberInviteCodeRepository;
    private final HouseHoldRepository houseHoldRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void create(MemberCreateRequest request) {
        VerifyUtil.verifyText(request.getLoginId());
        VerifyUtil.verifyText(request.getPassword());
        VerifyUtil.verifyText(request.getName());
        VerifyUtil.verifyText(request.getInviteCode());
        Objects.requireNonNull(request.getHouseholdId());

        MemberInvite memberInvite = memberInviteCodeRepository.findByCode(request.getInviteCode())
                .orElseThrow(() -> new NotFoundException("not found inviteCode"));

        if(memberInvite.isExpired(dateUtils.today())){
            throw new ExpiredInviteCodeException("expired inviteCode");
        }

        HouseHold houseHold = houseHoldRepository.findById(request.getHouseholdId())
                .orElseThrow(() -> new NotFoundException("not found household"));

        Member member = MemberCreateRequest.newMember(request);
        memberRepository.save(member);

        houseHold.updateHouseHolder(HouseHolder.of(member, member.getName(), member.getMobile()));
    }
}
