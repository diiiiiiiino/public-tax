package com.nos.tax.member.command.application.service;

import com.nos.tax.helper.builder.HouseHoldCreateHelperBuilder;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.enumeration.HouseHoldState;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.enumeration.MemberState;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemberWithdrawServiceTest {

    private MemberRepository memberRepository;
    private HouseHoldRepository houseHoldRepository;
    private MemberWithdrawService memberWithdrawService;

    public MemberWithdrawServiceTest() {
        memberRepository = mock(MemberRepository.class);
        houseHoldRepository = mock(HouseHoldRepository.class);
        this.memberWithdrawService = new MemberWithdrawService(memberRepository, houseHoldRepository);
    }

    @DisplayName("탈퇴 하려는 회원이 존재하지 않을 때")
    @Test
    void withDrawMemberNotFound() {
        Member member = MemberCreateHelperBuilder.builder().build();
        assertThatThrownBy(() -> memberWithdrawService.withDraw(member))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("Member not found");
    }

    @DisplayName("회원에 매핑된 세대가 존재하지 않을 때")
    @Test
    void householdNotFound() {
        Member member = MemberCreateHelperBuilder.builder().build();

        when(memberRepository.findByLoginIdAndState(anyString(), any(MemberState.class))).thenReturn(Optional.of(member));

        assertThatThrownBy(() -> memberWithdrawService.withDraw(member))
                .isInstanceOf(HouseHoldNotFoundException.class)
                .hasMessage("HouseHold not found");
    }

    @DisplayName("회원 탈퇴 성공")
    @Test
    void memberWithdraw() {
        Member member = MemberCreateHelperBuilder.builder().id(1L).build();
        HouseHold houseHold = HouseHoldCreateHelperBuilder.builder().member(member).build();

        when(memberRepository.findByLoginIdAndState(anyString(), any(MemberState.class))).thenReturn(Optional.of(member));
        when(houseHoldRepository.findByMemberId(anyLong())).thenReturn(Optional.of(houseHold));

        memberWithdrawService.withDraw(member);

        assertThat(member.getState()).isEqualTo(MemberState.DEACTIVATION);
        assertThat(houseHold.getMembers()).isEmpty();
        assertThat(houseHold.getHouseHoldState()).isEqualTo(HouseHoldState.EMPTY);
    }
}
