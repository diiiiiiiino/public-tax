package com.nos.tax.building.command.domain;

import com.nos.tax.building.command.domain.Address;
import com.nos.tax.building.command.domain.Building;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.helper.builder.HouseHolderCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BuildingAggregationTest {

    @DisplayName("주소 생성 시 파라미터에 null이나 빈 값 전달")
    @ParameterizedTest
    @MethodSource("provideArgsForAddress")
    void address_create_with_null_and_empty_parameter(String address1, String address2, String zipNo){
        assertThatThrownBy(() -> Address.of(address1, address2, zipNo))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("건물 생성 시 건물명 누락")
    @ParameterizedTest
    @NullAndEmptySource
    void building_create_with_null_and_empty_name(String name) {
        Assertions.assertThatThrownBy(() -> BuildingCreateHelperBuilder.builder()
                        .buildingName(name)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("건물 엔티티 생성 시 주소 누락")
    @Test
    void building_create_with_null_address() {
        Assertions.assertThatThrownBy(() -> BuildingCreateHelperBuilder.builder()
                        .address(null)
                        .build())
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("건물 엔티티 생성 시 비어있는 세대 목록을 생성할 경우")
    @Test
    void building_create_with_null_households() {
        Assertions.assertThatThrownBy(() -> BuildingCreateHelperBuilder.builder()
                        .houseHolds(List.of())
                        .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessage("no buildingFunctions");
    }

    @DisplayName("건물 엔티티 생성 성공")
    @Test
    void building_create_success() {
        Building building = BuildingCreateHelperBuilder.builder().build();

        assertThat(building).isNotNull();
        assertThat(building.getName()).isEqualTo("빌라");
    }

    @DisplayName("건물 엔티티 건물명 수정 시 null이나 빈 값 전달 ")
    @ParameterizedTest
    @NullAndEmptySource
    void building_name_update_with_null_and_empty(String name){
        Building building = BuildingCreateHelperBuilder.builder().build();

        assertThatThrownBy(() -> building.changeName(name))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("건물 엔티티 건물명 수정 성공")
    @Test
    void building_name_update_success(){
        Building building = BuildingCreateHelperBuilder.builder().build();

        building.changeName("래미안");

        assertThat(building.getName()).isEqualTo("래미안");
    }

    @DisplayName("건물 엔티티 주소 수정 실패")
    @ParameterizedTest
    @MethodSource("provideArgsForAddress")
    void building_address_update_fail(String address1, String address2, String zipNo){
        Building building = BuildingCreateHelperBuilder.builder().build();

        assertThatThrownBy(() -> building.changeAddress(address1, address2, zipNo))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("건물 엔티티 주소 수정 성공")
    @Test
    void building_address_update_success(){
        Building building = BuildingCreateHelperBuilder.builder().build();

        building.changeAddress("수정 주소1", "수정 주소2", "수정 우편번호");

        Address address = building.getAddress();

        assertThat(address).isNotNull();
        assertThat(address.getAddress1()).isEqualTo("수정 주소1");
        assertThat(address.getAddress2()).isEqualTo("수정 주소2");
        assertThat(address.getZipNo()).isEqualTo("수정 우편번호");
    }

    @DisplayName("건물 세대주 추가 시 비어있는 리스트 전달")
    @Test
    void building_householder_add_with_empty_list(){
        Building building = BuildingCreateHelperBuilder.builder().build();

        List<HouseHold> newHouseHolds = List.of();

        assertThatThrownBy(() -> building.addHouseHolds(newHouseHolds))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("건물 세대 추가 성공")
    @Test
    void building_householder_add_success(){
        Building building = BuildingCreateHelperBuilder.builder().build();

        List<HouseHold> newHouseHolds = List.of(HouseHold.of("102호", building));

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
}
