package com.nos.tax.building;

import com.nos.tax.building.domain.Address;
import com.nos.tax.building.domain.Building;
import com.nos.tax.household.domain.HouseHold;
import com.nos.tax.household.domain.HouseHolder;
import com.nos.tax.member.domain.Mobile;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BuildingAggregationTest {

    @DisplayName("주소 밸류 생성 실패")
    @ParameterizedTest
    @MethodSource("provideArgsForAddress")
    void whenAddressCreateThenIllegalArgumentsException(String address1, String address2, String zipNo){
        assertThatThrownBy(() -> Address.of(address1, address2, zipNo))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("건물 엔티티 주소 수정 성공")
    @Test
    void whenAddressCreateThenSuccess(){
        Address address = Address.of("주소1", "주소2", "우편번호");;

        assertThat(address).isNotNull();
        assertThat(address.getAddress1()).isEqualTo("주소1");
        assertThat(address.getAddress2()).isEqualTo("주소2");
        assertThat(address.getZipNo()).isEqualTo("우편번호");
    }

    @DisplayName("건물 엔티티 생성 시 건물명 누락")
    @ParameterizedTest
    @NullAndEmptySource
    void whenBuildingSaveThenNameNullPointerException(String name) {
        Address address = Address.of("서울시 동작구 사당동", "현대 아파트 101동", "111222");
        Mobile mobile = Mobile.of("010", "1111", "2222");
        HouseHolder houseHolder = HouseHolder.of("세대주", mobile);
        List<Function<Building, HouseHold>> buildingFunctions = List.of((building) -> HouseHold.of("101호", houseHolder, building));

        Assertions.assertThatThrownBy(() -> Building.of(name, address, buildingFunctions))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("건물 엔티티 생성 시 주소 누락")
    @Test
    void whenBuildingSaveThenAddressNullPointerException() {
        Address address = null;
        Mobile mobile = Mobile.of("010", "1111", "2222");
        HouseHolder houseHolder = HouseHolder.of("세대주", mobile);
        List<Function<Building, HouseHold>> buildingFunctions = List.of((building) -> HouseHold.of("101호", houseHolder, building));

        Assertions.assertThatThrownBy(() -> Building.of("현대빌라", address, buildingFunctions))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("건물 엔티티 생성 시 비어있는 세대 목록을 생성할 경우")
    @Test
    void whenBuildingSaveThenHouseHoldsNullPointerException() {
        Address address = Address.of("서울시 동작구 사당동", "현대 아파트 101동", "111222");
        List<Function<Building, HouseHold>> buildingFunctions = List.of();

        Assertions.assertThatThrownBy(() -> Building.of("현대빌라", address, buildingFunctions))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("no buildingFunctions");
    }

    @DisplayName("건물 엔티티 생성 성공")
    @Test
    void whenBuildingSaveThenSuccess() {
        Building building = getBuilding();

        assertThat(building).isNotNull();
        assertThat(building.getName()).isEqualTo("현대빌라");
    }

    @DisplayName("건물 엔티티 건물명 수정 실패")
    @ParameterizedTest
    @NullAndEmptySource
    void whenBuildingChangeNameThenIllegalArgumentsException(String name){
        Building building = getBuilding();

        assertThatThrownBy(() -> building.changeName(name))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("건물 엔티티 건물명 수정 성공")
    @Test
    void whenBuildingChangeNameThenSuccess(){
        Building building = getBuilding();

        building.changeName("래미안");

        assertThat(building.getName()).isEqualTo("래미안");
    }

    @DisplayName("건물 엔티티 주소 수정 실패")
    @ParameterizedTest
    @MethodSource("provideArgsForAddress")
    void whenBuildingChangeAddressThenIllegalArgumentsException(String address1, String address2, String zipNo){
        Building building = getBuilding();

        assertThatThrownBy(() -> building.changeAddress(address1, address2, zipNo))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("건물 엔티티 주소 수정 성공")
    @Test
    void whenBuildingChangeAddressThenSuccess(){
        Building building = getBuilding();

        building.changeAddress("수정 주소1", "수정 주소2", "수정 우편번호");

        Address address = building.getAddress();

        assertThat(address).isNotNull();
        assertThat(address.getAddress1()).isEqualTo("수정 주소1");
        assertThat(address.getAddress2()).isEqualTo("수정 주소2");
        assertThat(address.getZipNo()).isEqualTo("수정 우편번호");
    }

    @DisplayName("건물 세대주 추가 시 비어있는 리스트 전달")
    @Test
    void whenBuildingAddHouseholderThenNullPointerException(){
        Building building = getBuilding();

        List<HouseHold> newHouseHolds = List.of();

        assertThatThrownBy(() -> building.addHouseHolds(newHouseHolds))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("건물 세대주 추가 성공")
    @Test
    void whenBuildingAddHouseholderThenSuccess(){
        Building building = getBuilding();

        List<HouseHold> newHouseHolds = List.of(HouseHold.of("102호", HouseHolder.of("102호 세대주", Mobile.of("010", "2222", "3333")), building));

        building.addHouseHolds(newHouseHolds);

        assertThat(building.getHouseHolds()).hasSize(2);
    }

    private static Stream<Arguments> provideArgsForAddress(){
        return Stream.of(
                Arguments.of("", "1111", "2222"),
                Arguments.of(null, "1111", "2222"),
                Arguments.of("010", "", "2222"),
                Arguments.of("010", null, "2222"),
                Arguments.of("010", "1111", ""),
                Arguments.of("010", "1111", null)
        );
    }

    private Building getBuilding(){
        Address address = Address.of("서울시 동작구 사당동", "현대 아파트 101동", "111222");
        Mobile mobile = Mobile.of("010", "1111", "2222");
        HouseHolder houseHolder = HouseHolder.of("세대주", mobile);

        List<Function<Building, HouseHold>> buildingFunctions = new ArrayList<>(List.of((building) -> HouseHold.of("101호", houseHolder, building)));

        return Building.of("현대빌라", address, buildingFunctions);
    }
}
