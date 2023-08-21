package com.nos.tax.household.command.domain;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.helper.builder.HouseHolderCreateHelperBuilder;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.household.command.domain.enumeration.HouseHoldState;
import com.nos.tax.member.command.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HouseholdAggregationTest {

    @DisplayName("세대주 생성 시 이름 누락")
    @ParameterizedTest
    @NullAndEmptySource
    void householder_create_with_null_and_empty_name(String name){
        assertThatThrownBy(() -> HouseHolderCreateHelperBuilder.builder().name(name).build())
                .isInstanceOf(ValidationErrorException.class);
    }

    @DisplayName("세대주 생성 시 Mobile 밸류 누락")
    @Test
    void householder_create_with_null_mobile() {
        assertThatThrownBy(() -> HouseHolderCreateHelperBuilder.builder().mobile(null).build())
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("houseHolderMobile is null");
    }

    @DisplayName("세대주 생성 성공")
    @Test
    void householder_create_success() {
        HouseHolder houseHolder = HouseHolderCreateHelperBuilder.builder().build();

        assertThat(houseHolder.getName()).isEqualTo("세대주");
    }

    @DisplayName("세대 생성 시 세대명 누락")
    @ParameterizedTest
    @NullAndEmptySource
    void household_create_with_null_and_empty_room(String room) {
        assertThatThrownBy(() -> HouseHold.of(room, BuildingCreateHelperBuilder.builder().build()))
                .isInstanceOf(ValidationErrorException.class);
    }

    @DisplayName("세대 생성 성공")
    @Test
    void household_create_success() {
        HouseHold houseHold = HouseHold.of("101호", BuildingCreateHelperBuilder.builder().build());

        assertThat(houseHold.getRoom()).isEqualTo("101호");
        assertThat(houseHold.getHouseHoldState()).isEqualTo(HouseHoldState.EMPTY);
    }

    @DisplayName("세대 세대주 변경")
    @Test
    void household_householder_update() {
        HouseHold houseHold = HouseHold.of("101호", BuildingCreateHelperBuilder.builder().build());

        Member member = MemberCreateHelperBuilder.builder().build();
        HouseHolder houseHolder = HouseHolder.of(member, member.getName(), member.getMobile());

        houseHold.moveInHouse(houseHolder);
        assertThat(houseHold.getHouseHolder()).isNotNull();
    }
}
