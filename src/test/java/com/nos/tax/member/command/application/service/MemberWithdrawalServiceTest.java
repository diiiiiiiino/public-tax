package com.nos.tax.member.command.application.service;

import com.nos.tax.helper.builder.HouseHoldCreateHelperBuilder;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.enumeration.HouseHoldState;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemberWithdrawalServiceTest {

    private MemberRepository memberRepository;
    private HouseHoldRepository houseHoldRepository;
    private MemberWithdrawalService memberWithdrawalService;

    public MemberWithdrawalServiceTest() {
        memberRepository = mock(MemberRepository.class);
        houseHoldRepository = mock(HouseHoldRepository.class);
        this.memberWithdrawalService = new MemberWithdrawalService(memberRepository, houseHoldRepository);
    }

    @DisplayName("탈퇴 하려는 회원이 존재하지 않을 때")
    @Test
    void withDrawMemberNotFound() {
        Member member = MemberCreateHelperBuilder.builder().build();
        assertThatThrownBy(() -> memberWithdrawalService.withDraw(member))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("Member not found");
    }

    @DisplayName("회원에 매핑된 세대가 존재하지 않을 때")
    @Test
    void householdNotFound() {
        Member member = MemberCreateHelperBuilder.builder().build();

        when(memberRepository.findByLoginIdAndIsEnabled(anyString(), anyBoolean())).thenReturn(Optional.of(member));

        assertThatThrownBy(() -> memberWithdrawalService.withDraw(member))
                .isInstanceOf(HouseHoldNotFoundException.class)
                .hasMessage("HouseHold not found");
    }

    @DisplayName("회원 탈퇴 성공")
    @Test
    void memberWithdraw() {
        Member member = MemberCreateHelperBuilder.builder().id(1L).build();
        HouseHold houseHold = HouseHoldCreateHelperBuilder.builder().member(member).build();

        when(memberRepository.findByLoginIdAndIsEnabled(anyString(), anyBoolean())).thenReturn(Optional.of(member));
        when(houseHoldRepository.findByMemberId(anyLong())).thenReturn(Optional.of(houseHold));

        memberWithdrawalService.withDraw(member);

        assertThat(member.isEnabled()).isFalse();
        assertThat(houseHold.getHouseHolder()).isNull();
        assertThat(houseHold.getHouseHoldState()).isEqualTo(HouseHoldState.EMPTY);
    }
}