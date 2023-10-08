package com.nos.tax.watermeter.command.application;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.helper.builder.HouseHoldCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.waterbill.command.domain.WaterBill;
import com.nos.tax.waterbill.command.domain.repository.WaterBillRepository;
import com.nos.tax.watermeter.command.application.exception.WaterMeterDeleteStateException;
import com.nos.tax.watermeter.command.application.exception.WaterMeterNotFoundException;
import com.nos.tax.watermeter.command.application.service.WaterMeterDeleteService;
import com.nos.tax.watermeter.command.domain.WaterMeter;
import com.nos.tax.watermeter.command.domain.WaterMeterState;
import com.nos.tax.watermeter.command.domain.repository.WaterMeterRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WaterMeterDeleteServiceTest {

    private WaterMeterRepository waterMeterRepository;
    private WaterBillRepository waterBillRepository;
    private WaterMeterDeleteService waterMeterDeleteService;

    public WaterMeterDeleteServiceTest() {
        waterMeterRepository = mock(WaterMeterRepository.class);
        waterBillRepository = mock(WaterBillRepository.class);
        waterMeterDeleteService = new WaterMeterDeleteService(waterMeterRepository, waterBillRepository);
    }

    @DisplayName("수도 계량 삭제 시 미조회")
    @Test
    void whenWaterMeterDeleteThenNotFound() {
        Assertions.assertThatThrownBy(() -> waterMeterDeleteService.delete(1L))
                .isInstanceOf(WaterMeterNotFoundException.class)
                .hasMessage("WaterMeter not found");
    }

    @DisplayName("수도 계량 삭제 시 수도 요금 정산 중일때")
    @Test
    void whenWaterMeterDeleteThenWaterBillProcessing() {
        Building building = BuildingCreateHelperBuilder.builder().build();
        WaterBill waterBill = WaterBill.of(building, 200000, YearMonth.of(2023, 10));

        HouseHold houseHold = HouseHoldCreateHelperBuilder.builder().build();
        WaterMeter waterMeter = WaterMeter.of(1L, 100, 200, YearMonth.of(2023, 10), houseHold);

        when(waterMeterRepository.findByIdAndState(anyLong(), any(WaterMeterState.class))).thenReturn(Optional.of(waterMeter));
        when(waterBillRepository.findByWaterMeter(anyLong())).thenReturn(Optional.of(waterBill));

        Assertions.assertThatThrownBy(() -> waterMeterDeleteService.delete(1L))
                .isInstanceOf(WaterMeterDeleteStateException.class)
                .hasMessage("WaterMeter can be deleted before the water bill is settled");
    }

    @DisplayName("수도 계량 삭제 성공")
    @Test
    void whenWaterMeterDeleteSuccess() {
        HouseHold houseHold = HouseHoldCreateHelperBuilder.builder().build();
        WaterMeter waterMeter = WaterMeter.of(1L, 100, 200, YearMonth.of(2023, 10), houseHold);

        when(waterMeterRepository.findByIdAndState(anyLong(), any(WaterMeterState.class))).thenReturn(Optional.of(waterMeter));

        waterMeterDeleteService.delete(1L);

        assertThat(waterMeter.getState()).isEqualTo(WaterMeterState.DEACTIVATION);
    }
}
