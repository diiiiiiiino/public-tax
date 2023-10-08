package com.nos.tax.watermeter.command.application;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.builder.HouseHoldCreateHelperBuilder;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.watermeter.command.application.dto.WaterMeterCreateRequest;
import com.nos.tax.watermeter.command.application.service.WaterMeterCreateService;
import com.nos.tax.watermeter.command.application.validator.WaterMeterCreateRequestValidator;
import com.nos.tax.watermeter.command.domain.WaterMeter;
import com.nos.tax.watermeter.command.domain.exception.PresentMeterSmallerException;
import com.nos.tax.watermeter.command.domain.repository.WaterMeterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;

import java.time.YearMonth;
import java.util.List;
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
    private WaterMeterCreateRequestValidator validator;

    public WaterMeterCreateServiceTest() {
        houseHoldRepository = mock(HouseHoldRepository.class);
        waterMeterRepository = mock(WaterMeterRepository.class);
        validator = new WaterMeterCreateRequestValidator();
        waterMeterCreateService = new WaterMeterCreateService(houseHoldRepository, waterMeterRepository, validator);
    }

    @DisplayName("수도요금 정산 생성 시 파라미터 유효성 오류")
    @Test
    void requestValueInvalid() {
        Member member = MemberCreateHelperBuilder.builder().id(1L).build();
        WaterMeterCreateRequest request = WaterMeterCreateRequest.of(-10, -20, null);

        assertThatThrownBy(() -> waterMeterCreateService.create(member.getId(), request))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("Request has invalid values")
                .hasFieldOrPropertyWithValue("errors", List.of(
                        ValidationError.of("previousMeter", ValidationCode.NEGATIVE.getValue()),
                        ValidationError.of("presentMeter", ValidationCode.NEGATIVE.getValue()),
                        ValidationError.of("calculateYm", ValidationCode.NULL.getValue())
                ));
    }

    @DisplayName("이번 달 수도 계량 값이 저번 달 수동 계량 값보다 작은 경우")
    @Test
    void previousMeterBiggerThanPresentMeter() {
        WaterMeterCreateRequest waterMeterCreateRequest = new WaterMeterCreateRequest(100, 99, YearMonth.of(2023, 8));
        Member member = MemberCreateHelperBuilder.builder().id(1L).build();

        when(houseHoldRepository.findByMemberId(any())).thenReturn(Optional.of(HouseHoldCreateHelperBuilder.builder().build()));

        assertThatThrownBy(() -> waterMeterCreateService.create(member.getId(), waterMeterCreateRequest))
                .isInstanceOf(PresentMeterSmallerException.class)
                .hasMessage("Present meter smaller than previous meter");
    }

    @DisplayName("회원이 관리하는 세대가 없을 경우")
    @Test
    void householdIsNotFound(){
        WaterMeterCreateRequest waterMeterCreateRequest = new WaterMeterCreateRequest(0, 100, YearMonth.of(2023, 8));
        Member member = MemberCreateHelperBuilder.builder().id(1L).build();

        when(houseHoldRepository.findByMemberId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> waterMeterCreateService.create(member.getId(), waterMeterCreateRequest))
                .isInstanceOf(HouseHoldNotFoundException.class)
                .hasMessage("Household not found");
    }

    @DisplayName("수도 계량 데이터 생성 성공")
    @Test
    void createSuccess(){
        WaterMeterCreateRequest request = new WaterMeterCreateRequest(0, 100, YearMonth.of(2023, 8));
        Member member = MemberCreateHelperBuilder.builder().id(1L).build();
        HouseHold houseHold = HouseHoldCreateHelperBuilder.builder().build();

        when(houseHoldRepository.findByMemberId(any())).thenReturn(Optional.of(houseHold));
        when(waterMeterRepository.save(any())).thenReturn(WaterMeter.of(1L, request.getPreviousMeter(), request.getPresentMeter(), request.getCalculateYm(), houseHold));

        waterMeterCreateService.create(member.getId(), request);

        ArgumentCaptor<WaterMeter> captor = ArgumentCaptor.forClass(WaterMeter.class);
        BDDMockito.then(waterMeterRepository).should().save(captor.capture());

        WaterMeter waterMeter = captor.getValue();
        assertThat(waterMeter.getPreviousMeter()).isEqualTo(0);
        assertThat(waterMeter.getPresentMeter()).isEqualTo(100);
        assertThat(waterMeter.getCalculateYm()).isEqualTo(YearMonth.of(2023, 8));
    }
}
