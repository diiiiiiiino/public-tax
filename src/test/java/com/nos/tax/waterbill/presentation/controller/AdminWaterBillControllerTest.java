package com.nos.tax.waterbill.presentation.controller;

import com.nos.tax.building.command.application.BuildingNotFoundException;
import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.BaseControllerTest;
import com.nos.tax.waterbill.command.application.dto.WaterBillCreateRequest;
import com.nos.tax.waterbill.command.application.exception.WaterBillNotFoundException;
import com.nos.tax.waterbill.command.application.service.WaterBillCalculateAppService;
import com.nos.tax.waterbill.command.application.service.WaterBillCreateService;
import com.nos.tax.waterbill.command.domain.exception.WaterBillDuplicateException;
import com.nos.tax.waterbill.command.domain.exception.WaterBillNotCalculateStateException;
import com.nos.tax.waterbill.command.domain.exception.WaterBillNotReadyStateException;
import com.nos.tax.waterbill.command.domain.exception.WaterMeterNotAllCreatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class AdminWaterBillControllerTest extends BaseControllerTest {

    @MockBean
    private WaterBillCreateService waterBillCreateService;

    @MockBean
    private WaterBillCalculateAppService waterBillCalculateAppService;

    @BeforeEach
    void beforeEach() throws Exception {
        login("admin", "qwer1234!@");
    }

    @DisplayName("수도요금 정산 데이터 생성 파라미터 유효성 에러")
    @Test
    void whenWaterBillCreateThenInvalidRequest() throws Exception {
        WaterBillCreateRequest request = WaterBillCreateRequest.of(40000, YearMonth.of(2023, 8));

        List<ValidationError> errors = new ArrayList<>();
        errors.add(ValidationError.of("totalAmount", ValidationCode.NEGATIVE.getValue()));
        errors.add(ValidationError.of("calculateYm", ValidationCode.NULL.getValue()));

        doThrow(new ValidationErrorException("Request has invalid values", errors))
                .when(waterBillCreateService).create(any(), any(WaterBillCreateRequest.class));

        mvcPerform(post("/admin/water-bill"), request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @DisplayName("수도요금 정산 데이터 생성 시 건물 미조회")
    @Test
    void whenWaterBillCreateThenMemberNotFound() throws Exception {
        WaterBillCreateRequest request = WaterBillCreateRequest.of(40000, YearMonth.of(2023, 8));

        doThrow(new BuildingNotFoundException("Building not found"))
                .when(waterBillCreateService).create(any(), any(WaterBillCreateRequest.class));

        mvcPerform(post("/admin/water-bill"), request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("BuildingNotFound"));
    }

    @DisplayName("수도요금 정산 데이터 생성 시 정산 데이터가 존재할때")
    @Test
    void whenWaterBillCreateThenWaterBillDuplication() throws Exception {
        WaterBillCreateRequest request = WaterBillCreateRequest.of(40000, YearMonth.of(2023, 8));

        doThrow(new WaterBillDuplicateException("2023-08 WaterBill is exists"))
                .when(waterBillCreateService).create(any(), any(WaterBillCreateRequest.class));

        mvcPerform(post("/admin/water-bill"), request)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("WaterBillDuplicate"));
    }

    @DisplayName("수도요금 정산 데이터 생성 성공")
    @Test
    void whenWaterBillCreateThenSuccess() throws Exception {
        WaterBillCreateRequest request = WaterBillCreateRequest.of(40000, YearMonth.of(2023, 8));

        mvcPerform(post("/admin/water-bill"), request)
                .andExpect(status().isOk());
    }

    @DisplayName("수도요금 계산 시 파라미터 유효성 에러")
    @Test
    void whenWaterBillCalculateThenInvalidRequest() throws Exception {
        List<ValidationError> errors = new ArrayList<>();
        errors.add(ValidationError.of("memberId", ValidationCode.NULL.getValue()));
        errors.add(ValidationError.of("calculateYm", ValidationCode.NULL.getValue()));

        doThrow(new ValidationErrorException("Request has invalid values", errors))
                .when(waterBillCalculateAppService).calculate(any(), any(YearMonth.class));

        mvcPerform(post("/admin/water-bill/calculate/2023-08"), null)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @DisplayName("수도요금 계산 시 건물 미조회")
    @Test
    void whenWaterBillCalculateThenBuildingNotFound() throws Exception {
        doThrow(new BuildingNotFoundException("Building not found"))
                .when(waterBillCalculateAppService).calculate(any(), any(YearMonth.class));

        mvcPerform(post("/admin/water-bill/calculate/2023-08"), null)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("BuildingNotFound"));
    }

    @DisplayName("수도요금 계산 시 수도요금 데이터 미조회")
    @Test
    void whenWaterBillCalculateThenNotFound() throws Exception {
        doThrow(new WaterBillNotFoundException("WaterBill not found"))
                .when(waterBillCalculateAppService).calculate(any(), any(YearMonth.class));

        mvcPerform(post("/admin/water-bill/calculate/2023-08"), null)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("WaterBillNotFound"));
    }

    @DisplayName("수도요금 계산 시 수도 계량 데이터가 세대수 만큼 생성되지 않았을 경우")
    @Test
    void whenWaterBillCalculateThenWaterMeterNotAllCreated() throws Exception {
        doThrow(new WaterMeterNotAllCreatedException("Water meter not all created"))
                .when(waterBillCalculateAppService).calculate(any(), any(YearMonth.class));

        mvcPerform(post("/admin/water-bill/calculate/2023-08"), null)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value("WaterMeterNotAllCreated"));
    }

    @DisplayName("수도요금 계산 시 계산 상태가 준비 상태가 아닌 경우")
    @Test
    void whenWaterBillCalculateThenNotReadyState() throws Exception {
        doThrow(new WaterBillNotReadyStateException("You can only calculate the water bill when you are ready"))
                .when(waterBillCalculateAppService).calculate(any(), any(YearMonth.class));

        mvcPerform(post("/admin/water-bill/calculate/2023-08"), null)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("WaterBillNotReadyState"));
    }

    @DisplayName("수도요금 계산 중 WaterBillDetail 추가 시 계산 상태가 아닌 경우")
    @Test
    void whenWaterBillDetailAddThenNotCalculateState() throws Exception {
        doThrow(new WaterBillNotCalculateStateException("WaterBillDetail addition is possible when calculating state"))
                .when(waterBillCalculateAppService).calculate(any(), any(YearMonth.class));

        mvcPerform(post("/admin/water-bill/calculate/2023-08"), null)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("WaterBillNotCalculateState"));
    }

    @DisplayName("수도요금 계산 성공")
    @Test
    void whenWaterBillCalculateThenSuccess() throws Exception {
        mvcPerform(post("/admin/water-bill/calculate/2023-08"), null)
                .andExpect(status().isOk());
    }
}
