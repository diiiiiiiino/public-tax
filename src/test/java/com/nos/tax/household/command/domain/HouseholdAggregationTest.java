package com.nos.tax.household.command.domain;

import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.helper.builder.HouseHolderCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.HouseHolder;
import com.nos.tax.member.command.domain.Mobile;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HouseholdAggregationTest {

    @DisplayName("전화번호 밸류 생성 시 값 누락")
    @ParameterizedTest
    @MethodSource("provideArgsForMobile")
    void whenMobileSaveThenIllegalArgumentException(String firstNo, String secondNo, String threeNo) {
        Assertions.assertThatThrownBy(() -> Mobile.of(firstNo, secondNo, threeNo))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전화번호 밸류 생성 성공")
    @Test
    void whenMobileSaveThenSuccess(){
        Mobile mobile = Mobile.of("010", "1111", "2222");

        assertThat(mobile.toString()).isEqualTo("010-1111-2222");
    }

    @DisplayName("세대주 밸류 생성 시 이름 누락")
    @ParameterizedTest
    @NullAndEmptySource
    void whenHouseHolderSaveThenNameIllegalArgumentException(String name){
        assertThatThrownBy(() -> HouseHolderCreateHelperBuilder.builder().name(name).build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("세대주 밸류 생성 시 Mobile 밸류 누락")
    @Test
    void whenHouseHolderSaveThenMobileIllegalArgumentException() {
        assertThatThrownBy(() -> HouseHolderCreateHelperBuilder.builder().mobile(null).build())
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("세대주 밸류 생성 성공")
    @Test
    void whenHouseHolderSaveThenSuccess() {
        HouseHolder houseHolder = HouseHolderCreateHelperBuilder.builder().build();

        assertThat(houseHolder.getName()).isEqualTo("세대주");
    }

    @DisplayName("세대 밸류 생성 시 세대명 누락")
    @ParameterizedTest
    @NullAndEmptySource
    void whenHouseHoldSaveThenRoomIllegalArgumentException(String room) {
        HouseHolder houseHolder = HouseHolderCreateHelperBuilder.builder().build();

        assertThatThrownBy(() -> HouseHold.of(room, houseHolder, BuildingCreateHelperBuilder.builder().build()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("세대 밸류 생성 시 세대주 누락")
    @Test
    void whenHouseHoldSaveThenHouseHolderNullPointerException() {
        HouseHolder houseHolder = null;

        assertThatThrownBy(() -> HouseHold.of("세대주", houseHolder, BuildingCreateHelperBuilder.builder().build()))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("세대 밸류 생성 성공")
    @Test
    void whenHouseHoldSaveThenSuccess() {
        HouseHolder houseHolder = HouseHolderCreateHelperBuilder.builder().build();

        HouseHold houseHold = HouseHold.of("101호", houseHolder, BuildingCreateHelperBuilder.builder().build());

        assertThat(houseHold.getRoom()).isEqualTo("101호");
    }

    private static Stream<Arguments> provideArgsForMobile(){
        return Stream.of(
                Arguments.of("", "1111", "2222"),
                Arguments.of(null, "1111", "2222"),
                Arguments.of("010", "", "2222"),
                Arguments.of("010", null, "2222"),
                Arguments.of("010", "1111", ""),
                Arguments.of("010", "1111", null)
        );
    }
}
