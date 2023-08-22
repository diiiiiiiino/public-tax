package com.nos.tax.waterbill.command.application.controller;

import com.nos.tax.building.command.application.BuildingNotFoundException;
import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.BaseControllerTest;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.waterbill.command.application.dto.WaterBillCreateRequest;
import com.nos.tax.waterbill.command.application.service.WaterBillCreateService;
import com.nos.tax.waterbill.command.application.validator.WaterBillCreateRequestValidator;
import com.nos.tax.waterbill.command.domain.exception.WaterBillDuplicateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WaterBillController.class)
public class WaterBillControllerTest extends BaseControllerTest {

    @MockBean
    private WaterBillCreateService waterBillCreateService;


    @DisplayName("수도요금 정산 데이터 생성 파라미터 유효성 에러")
    @Test
    void whenWaterBillCreateThenInvalidRequest() throws Exception {
        WaterBillCreateRequest request = WaterBillCreateRequest.of(40000, YearMonth.of(2023, 8));

        List<ValidationError> errors = new ArrayList<>();
        errors.add(ValidationError.of("totalAmount", ValidationCode.NEGATIVE.getValue()));
        errors.add(ValidationError.of("calculateYm", ValidationCode.NULL.getValue()));

        doThrow(new ValidationErrorException("Request has invalid values", errors))
                .when(waterBillCreateService).create(any(Member.class), any(WaterBillCreateRequest.class));

        mockMvc.perform(post("/water-bill")
                        .content(writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @DisplayName("수도요금 정산 데이터 생성 시 멤버 미조회")
    @Test
    void whenWaterBillCreateThenMemberNotFound() throws Exception {
        WaterBillCreateRequest request = WaterBillCreateRequest.of(40000, YearMonth.of(2023, 8));

        doThrow(new BuildingNotFoundException("Building not found"))
                .when(waterBillCreateService).create(any(Member.class), any(WaterBillCreateRequest.class));

        mockMvc.perform(post("/water-bill")
                        .content(writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("수도요금 정산 데이터 생성 시 정산 데이터가 존재할때")
    @Test
    void whenWaterBillCreateThenWaterBillDuplication() throws Exception {
        WaterBillCreateRequest request = WaterBillCreateRequest.of(40000, YearMonth.of(2023, 8));

        doThrow(new WaterBillDuplicateException("2023-08 WaterBill is exists"))
                .when(waterBillCreateService).create(any(Member.class), any(WaterBillCreateRequest.class));

        mockMvc.perform(post("/water-bill")
                        .content(writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("수도요금 정산 데이터 생성 성공")
    @Test
    void whenWaterBillCreateThenSuccess() throws Exception {
        WaterBillCreateRequest request = WaterBillCreateRequest.of(40000, YearMonth.of(2023, 8));

        mockMvc.perform(post("/water-bill")
                        .content(writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
