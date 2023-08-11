package com.nos.tax.watermeter.command.application;

import com.nos.tax.application.exception.NotFoundException;
import com.nos.tax.helper.builder.HouseHoldCreateHelperBuilder;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.watermeter.command.domain.repository.WaterMeter;
import com.nos.tax.watermeter.command.domain.repository.WaterMeterRepository;
import com.nos.tax.watermeter.query.WaterMeterCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;

import java.time.YearMonth;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WaterMeterCreateServiceTest {

    private WaterMeterCreateService waterMeterCreateService;
    private HouseHoldRepository houseHoldRepository;
    private WaterMeterRepository waterMeterRepository;

    public WaterMeterCreateServiceTest() {
        houseHoldRepository = mock(HouseHoldRepository.class);
        waterMeterRepository = mock(WaterMeterRepository.class);
        waterMeterCreateService = new WaterMeterCreateService(houseHoldRepository, waterMeterRepository);
    }

    @DisplayName("지난 달 수도 계량 값이 음수인 경우")
    @Test
    void previousMeter_is_negative() {
        WaterMeterCreateRequest waterMeterCreateRequest = new WaterMeterCreateRequest(-1, 0, YearMonth.of(2023, 8));
        Member member = MemberCreateHelperBuilder.builder().build();

        when(houseHoldRepository.findByMemberId(any())).thenReturn(Optional.of(HouseHoldCreateHelperBuilder.builder().build()));

        assertThatThrownBy(() -> waterMeterCreateService.create(waterMeterCreateRequest, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("no negative");
    }

    @DisplayName("이번 달 수도 계량 값이 음수인 경우")
    @Test
    void presentMeter_is_negative() {
        WaterMeterCreateRequest waterMeterCreateRequest = new WaterMeterCreateRequest(0, -1, YearMonth.of(2023, 8));
        Member member = MemberCreateHelperBuilder.builder().build();

        when(houseHoldRepository.findByMemberId(any())).thenReturn(Optional.of(HouseHoldCreateHelperBuilder.builder().build()));

        assertThatThrownBy(() -> waterMeterCreateService.create(waterMeterCreateRequest, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("no negative");
    }

    @DisplayName("이번 달 수도 계량 값이 저번 달 수동 계량 값보다 작은 경우")
    @Test
    void previous_meter_bigger_than_present_meter() {
        WaterMeterCreateRequest waterMeterCreateRequest = new WaterMeterCreateRequest(100, 99, YearMonth.of(2023, 8));
        Member member = MemberCreateHelperBuilder.builder().build();

        when(houseHoldRepository.findByMemberId(any())).thenReturn(Optional.of(HouseHoldCreateHelperBuilder.builder().build()));

        assertThatThrownBy(() -> waterMeterCreateService.create(waterMeterCreateRequest, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Present meter smaller than previous meter");
    }

    @DisplayName("수도계량 년월이 null인 경우")
    @Test
    void yearMonth_is_null() {
        WaterMeterCreateRequest waterMeterCreateRequest = new WaterMeterCreateRequest(0, 100, null);
        Member member = MemberCreateHelperBuilder.builder().build();

        when(houseHoldRepository.findByMemberId(any())).thenReturn(Optional.of(HouseHoldCreateHelperBuilder.builder().build()));

        assertThatThrownBy(() -> waterMeterCreateService.create(waterMeterCreateRequest, member))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("회원이 관리하는 세대가 없을 경우")
    @Test
    void household_is_not_found(){
        WaterMeterCreateRequest waterMeterCreateRequest = new WaterMeterCreateRequest(0, 100, YearMonth.of(2023, 8));
        Member member = MemberCreateHelperBuilder.builder().build();

        when(houseHoldRepository.findByMemberId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> waterMeterCreateService.create(waterMeterCreateRequest, member))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Household not found");
    }

    @DisplayName("수도 계량 데이터 생성 성공")
    @Test
    void create_success(){
        WaterMeterCreateRequest request = new WaterMeterCreateRequest(0, 100, YearMonth.of(2023, 8));
        Member member = MemberCreateHelperBuilder.builder().build();
        HouseHold houseHold = HouseHoldCreateHelperBuilder.builder().build();

        when(houseHoldRepository.findByMemberId(any())).thenReturn(Optional.of(houseHold));
        when(waterMeterRepository.save(any())).thenReturn(WaterMeter.of(1L, request.getPreviousMeter(), request.getPresentMeter(), request.getYearMonth(), houseHold));

        waterMeterCreateService.create(request, member);

        ArgumentCaptor<WaterMeter> captor = ArgumentCaptor.forClass(WaterMeter.class);
        BDDMockito.then(waterMeterRepository).should().save(captor.capture());

        WaterMeter waterMeter = captor.getValue();
        assertThat(waterMeter.getPreviousMeter()).isEqualTo(0);
        assertThat(waterMeter.getPresentMeter()).isEqualTo(100);
        assertThat(waterMeter.getYearMonth()).isEqualTo(YearMonth.of(2023, 8));
    }
}
