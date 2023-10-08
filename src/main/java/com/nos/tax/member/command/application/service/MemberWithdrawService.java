package com.nos.tax.member.command.application.service;

import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.enumeration.MemberState;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberWithdrawService {

    private final MemberRepository memberRepository;
    private final HouseHoldRepository houseHoldRepository;

    @Transactional
    public void withDraw(Member member) {
        member = memberRepository.findByLoginIdAndState(member.getLoginId(), MemberState.ACTIVATION)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        HouseHold houseHold = houseHoldRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new HouseHoldNotFoundException("HouseHold not found"));

        member.updateState(MemberState.DEACTIVATION);
        houseHold.moveOutHouse();
    }
}
