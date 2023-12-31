package com.nos.tax.household.command.application.service;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.builder.HouseHoldCreateHelperBuilder;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.household.command.application.HouseHolderChangeService;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.HouseHolder;
import com.nos.tax.household.command.domain.enumeration.HouseHoldState;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HouseHolderChangeServiceTest {

    private HouseHoldRepository houseHoldRepository;
    private MemberRepository memberRepository;
    private HouseHolderChangeService householderChangeService;

    public HouseHolderChangeServiceTest() {
        houseHoldRepository = mock(HouseHoldRepository.class);
        memberRepository = mock(MemberRepository.class);
        householderChangeService = new HouseHolderChangeService(houseHoldRepository, memberRepository);
    }

    @DisplayName("세대주 변경 시 파라미터 유효성 오류")
    @Test
    void requestValueInvalid() {
        assertThatThrownBy(() -> householderChangeService.change(null, null))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("Request has invalid values")
                .hasFieldOrPropertyWithValue("errors", List.of(
                        ValidationError.of("houseHoldId", ValidationCode.NULL.getValue()),
                        ValidationError.of("memberId", ValidationCode.NULL.getValue())
                ));
    }

    @DisplayName("세대가 조회되지 않을 때")
    @Test
    void houseHoldNotFound() {
        Assertions.assertThatThrownBy(() -> householderChangeService.change(1L, 1L))
                .isInstanceOf(HouseHoldNotFoundException.class)
                .hasMessage("HouseHold not found");
    }

    @DisplayName("회원이 조회되지 않을 때")
    @Test
    void memberNotFound() {
        HouseHold houseHold = HouseHoldCreateHelperBuilder.builder().build();

        when(houseHoldRepository.findByIdAndHouseHoldState(anyLong(), any(HouseHoldState.class))).thenReturn(Optional.of(houseHold));

        Assertions.assertThatThrownBy(() -> householderChangeService.change(1L, 1L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("Member not found");
    }

    @DisplayName("세대주 변경")
    @Test
    void changeHouseHolder() {
        Member member = MemberCreateHelperBuilder.builder().id(1L).build();
        HouseHold houseHold = HouseHoldCreateHelperBuilder.builder().build();
        houseHold.moveInHouse(HouseHolder.of(member));

        Member changeMember = MemberCreateHelperBuilder.builder().id(2L)
                .loginId("change")
                .name("변경자")
                .mobile(Mobile.of("01099998888"))
                .build();

        when(houseHoldRepository.findByIdAndHouseHoldState(anyLong(), any(HouseHoldState.class))).thenReturn(Optional.of(houseHold));
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(changeMember));

        householderChangeService.change(1L, 2L);

        HouseHolder houseHolder = houseHold.getHouseHolder();
        assertThat(houseHolder.getName()).isEqualTo("변경자");
        assertThat(houseHolder.getMobile().toString()).isEqualTo("01099998888");
        assertThat(houseHolder.getMember().getLoginId()).isEqualTo("change");
    }
}
