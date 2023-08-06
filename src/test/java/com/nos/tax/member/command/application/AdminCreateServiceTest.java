package com.nos.tax.member.command.application;

import com.nos.tax.building.command.domain.Address;
import com.nos.tax.building.command.domain.Building;
import com.nos.tax.building.command.domain.repository.BuildingRepository;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.member.command.application.dto.AdminCreateRequest;
import com.nos.tax.member.command.application.dto.BuildingInfo;
import com.nos.tax.member.command.application.dto.HouseHoldInfo;
import com.nos.tax.member.command.application.service.AdminCreateService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminCreateServiceTest {

    private BuildingRepository buildingRepository;
    private AdminCreateService adminCreateService;

    public AdminCreateServiceTest() {
        buildingRepository = mock(BuildingRepository.class);
        adminCreateService = new AdminCreateService(buildingRepository);
    }

    /**
     * 건물 정보가 없는 경우
     * 세대 목록이 없는 경우
     */
    @DisplayName("건물 정보가 없는 경우")
    @Test
    void create_admin_with_miss_building_info() {
        BuildingInfo buildingInfo = null;
        List<HouseHoldInfo> houseHoldInfos = List.of(HouseHoldInfo.of("101호"), HouseHoldInfo.of("102호"), HouseHoldInfo.of("103호"), HouseHoldInfo.of("104호"));

        AdminCreateRequest adminCreateRequest = AdminCreateRequest.of(buildingInfo, houseHoldInfos);

        Assertions.assertThatThrownBy(() -> adminCreateService.create(adminCreateRequest))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("세대 목록이 null 또는 비어있는 경우")
    @ParameterizedTest
    @NullAndEmptySource
    void create_admin_with_null_and_empty_households(List<HouseHoldInfo> houseHoldInfos) {
        BuildingInfo buildingInfo = BuildingInfo.of("광동빌라", "서울특별시 강남구 대치동", "광동빌라 A동", "123456");

        AdminCreateRequest adminCreateRequest = AdminCreateRequest.of(buildingInfo, houseHoldInfos);

        Assertions.assertThatThrownBy(() -> adminCreateService.create(adminCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("list no element");
    }

    @DisplayName("관리자 및 건물/세대 생성")
    @Test
    void create_admin_success() {
        BuildingInfo buildingInfo = BuildingInfo.of("광동빌라", "서울특별시 강남구 대치동", "광동빌라 A동", "123456");
        List<HouseHoldInfo> houseHoldInfos = List.of(HouseHoldInfo.of("101호"), HouseHoldInfo.of("102호"), HouseHoldInfo.of("103호"), HouseHoldInfo.of("104호"));

        AdminCreateRequest adminCreateRequest = AdminCreateRequest.of(buildingInfo, houseHoldInfos);

        Address address = Address.of("서울특별시 강남구 대치동", "광동빌라 A동", "123456");
        List<Function<Building, HouseHold>> houseHolds = List.of(
                building -> HouseHold.of("101호", building),
                building -> HouseHold.of("102호", building),
                building -> HouseHold.of("103호", building),
                building -> HouseHold.of("104호", building));

        Building building = BuildingCreateHelperBuilder.builder()
                .id(1L)
                .buildingName("광동빌라")
                .address(address)
                .houseHolds(houseHolds)
                .build();

        when(buildingRepository.save(any())).thenReturn(building);

        Long id = adminCreateService.create(adminCreateRequest);

        Assertions.assertThat(id).isEqualTo(1L);
    }
}
