package com.nos.tax.household.domain;

import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.member.domain.Mobile;
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
        Mobile mobile = Mobile.of("010", "1111", "2222");

        assertThatThrownBy(() -> HouseHolder.of(name, mobile))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("세대주 밸류 생성 시 Mobile 밸류 누락")
    @Test
    void whenHouseHolderSaveThenMobileIllegalArgumentException() {
        Mobile mobile = null;

        assertThatThrownBy(() -> HouseHolder.of("세대주", mobile))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("세대주 밸류 생성 성공")
    @Test
    void whenHouseHolderSaveThenSuccess() {
        Mobile mobile = Mobile.of("010", "1111", "2222");
        HouseHolder houseHolder = HouseHolder.of("세대주", mobile);

        assertThat(houseHolder.getName()).isEqualTo("세대주");
    }

    @DisplayName("세대 밸류 생성 시 세대명 누락")
    @ParameterizedTest
    @NullAndEmptySource
    void whenHouseHoldSaveThenRoomIllegalArgumentException(String room) {
        Mobile mobile = Mobile.of("010", "1111", "2222");
        HouseHolder houseHolder = HouseHolder.of("세대주", mobile);

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
        Mobile mobile = Mobile.of("010", "1111", "2222");
        HouseHolder houseHolder = HouseHolder.of("세대주", mobile);

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
