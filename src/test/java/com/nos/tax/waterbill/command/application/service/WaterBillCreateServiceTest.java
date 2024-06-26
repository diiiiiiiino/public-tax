package com.nos.tax.waterbill.command.application.service;

import com.nos.tax.building.command.application.BuildingNotFoundException;
import com.nos.tax.building.command.domain.Building;
import com.nos.tax.building.command.domain.BuildingState;
import com.nos.tax.building.command.domain.repository.BuildingRepository;
import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.waterbill.command.application.dto.WaterBillCreateRequest;
import com.nos.tax.waterbill.command.application.validator.WaterBillCreateRequestValidator;
import com.nos.tax.waterbill.command.domain.WaterBill;
import com.nos.tax.waterbill.command.domain.enumeration.WaterBillState;
import com.nos.tax.waterbill.command.domain.exception.WaterBillDuplicateException;
import com.nos.tax.waterbill.command.domain.repository.WaterBillRepository;
import com.nos.tax.watermeter.command.application.service.WaterMeterCreateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WaterBillCreateServiceTest {

    private BuildingRepository buildingRepository;
    private WaterBillCreateService waterBillCreateService;
    private WaterBillRepository waterBillRepository;
    private WaterMeterCreateService waterMeterCreateService;
    private WaterBillCreateRequestValidator validator;

    public WaterBillCreateServiceTest() {
        buildingRepository = mock(BuildingRepository.class);
        waterBillRepository = mock(WaterBillRepository.class);
        waterMeterCreateService = mock(WaterMeterCreateService.class);
        validator = new WaterBillCreateRequestValidator();
        waterBillCreateService = new WaterBillCreateService(buildingRepository, waterBillRepository, waterMeterCreateService, validator);
    }

    @DisplayName("수도요금 정산 생성 시 파라미터 유효성 오류")
    @Test
    void requestValueInvalid() {
        Member admin = MemberCreateHelperBuilder.builder().id(1L).build();
        WaterBillCreateRequest request = WaterBillCreateRequest.of(-44444, null);

        assertThatThrownBy(() -> waterBillCreateService.create(admin.getId(), request))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("Request has invalid values")
                .hasFieldOrPropertyWithValue("errors", List.of(
                        ValidationError.of("totalAmount", ValidationCode.NEGATIVE.getValue()),
                        ValidationError.of("calculateYm", ValidationCode.NULL.getValue())
                ));
    }

    @DisplayName("관리자의 건물이 조회되지 않을 때")
    @Test
    void whenCreateWaterBillThen_BuildingIsNotExists() {
        Member member = MemberCreateHelperBuilder.builder().id(1L).build();
        WaterBillCreateRequest request = WaterBillCreateRequest.of(77000, YearMonth.of(2023, 7));

        when(buildingRepository.findByMember(any(), any(BuildingState.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> waterBillCreateService.create(member.getId(), request))
                .isInstanceOf(BuildingNotFoundException.class)
                .hasMessage("Building not found");
    }

    @DisplayName("생성하고자하는 년월에 수도요금 정산데이터가 이미 존재할 떄")
    @Test
    void whenCreateWaterBillThenAlreadyCalculateYmExists() {
        Member member = MemberCreateHelperBuilder.builder().id(1L).build();

        Building building = BuildingCreateHelperBuilder.builder().build();
        WaterBill waterBill = WaterBill.of(building, 77000, YearMonth.of(2023, 7));
        WaterBillCreateRequest request = WaterBillCreateRequest.of(77000, YearMonth.of(2023, 7));

        when(buildingRepository.findByMember(any(), any(BuildingState.class))).thenReturn(Optional.of(building));
        when(waterBillRepository.findByBuildingAndCalculateYm(any(), any())).thenReturn(Optional.of(waterBill));

        assertThatThrownBy(() -> waterBillCreateService.create(member.getId(), request))
                .isInstanceOf(WaterBillDuplicateException.class)
                .hasMessage("2023-07 WaterBill is exists");
    }

    @DisplayName("관리자가 수도요금 정산 데이터 생성")
    @Test
    void adminCreateWaterBill() {
        Member admin = MemberCreateHelperBuilder.builder().id(1L).build();
        Building building = BuildingCreateHelperBuilder.builder().build();
        WaterBill waterBill = WaterBill.of(building, 77000, YearMonth.of(2023, 7));
        WaterBillCreateRequest request = WaterBillCreateRequest.of(77000, YearMonth.of(2023, 7));

        when(buildingRepository.findByMember(any(), any(BuildingState.class))).thenReturn(Optional.of(building));
        when(waterBillRepository.save(any())).thenReturn(waterBill);

        waterBill = waterBillCreateService.create(admin.getId(), request);

        assertThat(waterBill).isNotNull();
        assertThat(waterBill.getBuilding()).isEqualTo(building);
        assertThat(waterBill.getTotalAmount()).isEqualTo(77000);
        assertThat(waterBill.getState()).isEqualTo(WaterBillState.READY);
        assertThat(waterBill.getCalculateYm()).isEqualTo(YearMonth.of(2023, 7));
    }
}
