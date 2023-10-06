package com.nos.tax.member.command.application.service;

import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class MemberWithdrawalService {

    private final MemberRepository memberRepository;
    private final HouseHoldRepository houseHoldRepository;

    @Transactional
    public void withDraw(Member member) {
        member = memberRepository.findByLoginIdAndIsEnabled(member.getLoginId(), true)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        HouseHold houseHold = houseHoldRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new HouseHoldNotFoundException("HouseHold not found"));

        member.updateIsEnabled(false);
        houseHold.moveOutHouse();
    }
}
