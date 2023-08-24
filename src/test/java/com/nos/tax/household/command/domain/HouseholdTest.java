package com.nos.tax.household.command.domain;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.household.command.domain.enumeration.HouseHoldState;
import com.nos.tax.member.command.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HouseholdTest {

    @DisplayName("세대 생성 시 세대명 누락")
    @ParameterizedTest
    @NullAndEmptySource
    void householdCreateWithNullAndEmptyRoom(String room) {
        assertThatThrownBy(() -> HouseHold.of(room, BuildingCreateHelperBuilder.builder().build()))
                .isInstanceOf(ValidationErrorException.class);
    }

    @DisplayName("세대 생성 성공")
    @Test
    void householdCreateSuccess() {
        HouseHold houseHold = HouseHold.of("101호", BuildingCreateHelperBuilder.builder().build());

        assertThat(houseHold.getRoom()).isEqualTo("101호");
        assertThat(houseHold.getHouseHoldState()).isEqualTo(HouseHoldState.EMPTY);
    }

    @DisplayName("세대 세대주 변경")
    @Test
    void householdHouseholderUpdate() {
        HouseHold houseHold = HouseHold.of("101호", BuildingCreateHelperBuilder.builder().build());

        Member member = MemberCreateHelperBuilder.builder().build();
        HouseHolder houseHolder = HouseHolder.of(member, member.getName(), member.getMobile());

        houseHold.moveInHouse(houseHolder);
        assertThat(houseHold.getHouseHolder()).isNotNull();
    }
}
