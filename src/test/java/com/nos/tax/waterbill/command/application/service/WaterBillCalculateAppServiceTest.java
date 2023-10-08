package com.nos.tax.waterbill.command.application.service;

import com.nos.tax.building.command.application.BuildingNotFoundException;
import com.nos.tax.building.command.domain.Building;
import com.nos.tax.building.command.domain.BuildingState;
import com.nos.tax.building.command.domain.repository.BuildingRepository;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.waterbill.command.application.exception.WaterBillNotFoundException;
import com.nos.tax.waterbill.command.domain.WaterBill;
import com.nos.tax.waterbill.command.domain.enumeration.WaterBillState;
import com.nos.tax.waterbill.command.domain.repository.WaterBillRepository;
import com.nos.tax.waterbill.command.domain.service.WaterBillCalculateService;
import com.nos.tax.watermeter.command.domain.WaterMeter;
import com.nos.tax.watermeter.command.domain.repository.WaterMeterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WaterBillCalculateAppServiceTest {

    private WaterBillCalculateAppService waterBillCalculateAppService;
    private BuildingRepository buildingRepository;
    private WaterBillRepository waterBillRepository;
    private final WaterMeterRepository waterMeterRepository;
    private final WaterBillCalculateService waterBillCalculateService;

    public WaterBillCalculateAppServiceTest() {
        buildingRepository = mock(BuildingRepository.class);
        waterBillRepository = mock(WaterBillRepository.class);
        waterMeterRepository = mock(WaterMeterRepository.class);
        waterBillCalculateService = new WaterBillCalculateService();
        waterBillCalculateAppService = new WaterBillCalculateAppService(buildingRepository, waterBillRepository, waterMeterRepository, waterBillCalculateService);
    }

    @DisplayName("관리자가 관리하는 건물이 존재하지 않을때")
    @Test
    void buildingNotFound() {
        Member member = MemberCreateHelperBuilder.builder()
                .id(1L)
                .loginId("admin")
                .name("관리자")
                .build();

        assertThatThrownBy(() -> waterBillCalculateAppService.calculate(member.getId(), YearMonth.of(2023, 8)))
                .isInstanceOf(BuildingNotFoundException.class)
                .hasMessage("Building not found");
    }

    @DisplayName("수도요금 데이터가 존재하지 않을때")
    @Test
    void waterBillNotFound() {
        Member member = MemberCreateHelperBuilder.builder()
                .id(1L)
                .loginId("admin")
                .name("관리자")
                .build();

        Building building = BuildingCreateHelperBuilder.builder().build();

        when(buildingRepository.findByMember(anyLong(), any(BuildingState.class))).thenReturn(Optional.of(building));

        assertThatThrownBy(() -> waterBillCalculateAppService.calculate(member.getId(), YearMonth.of(2023, 8)))
                .isInstanceOf(WaterBillNotFoundException.class)
                .hasMessage("WaterBill not found");
    }

    @DisplayName("수도요금 계산 성공")
    @Test
    void waterBillCalculateSuccess() {
        Member member = MemberCreateHelperBuilder.builder()
                .id(1L)
                .loginId("admin")
                .name("관리자")
                .build();

        Building building = BuildingCreateHelperBuilder.builder().build();
        WaterBill waterBill = WaterBill.of(building, 50000, YearMonth.of(2023, 8));

        when(buildingRepository.findByMember(anyLong(), any(BuildingState.class))).thenReturn(Optional.of(building));
        when(waterBillRepository.findByBuildingAndCalculateYm(any(Building.class), any(YearMonth.class))).thenReturn(Optional.of(waterBill));
        when(waterMeterRepository.findAllByHouseHoldInAndCalculateYm(anyIterable(), any(YearMonth.class))).thenReturn(List.of(WaterMeter.of(0, 100, YearMonth.of(2023, 8), building.getHouseHolds().get(0))));

        waterBillCalculateAppService.calculate(member.getId(), YearMonth.of(2023, 8));

        assertThat(waterBill.getWaterBillDetails().size()).isEqualTo(1);
        assertThat(waterBill.getUnitAmount()).isEqualTo(500.0);
        assertThat(waterBill.getTotalUsage()).isEqualTo(100);
        assertThat(waterBill.getState()).isEqualTo(WaterBillState.COMPLETE);
    }
}
