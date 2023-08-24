package com.nos.tax.watermeter.command.presentation;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.BaseControllerTest;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.watermeter.command.application.dto.WaterMeterCreateRequest;
import com.nos.tax.watermeter.command.application.service.WaterMeterCreateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WaterMeterController.class)
public class WaterMeterControllerTest extends BaseControllerTest {

    @MockBean
    private WaterMeterCreateService waterMeterCreateService;

    @DisplayName("수도계량 데이터 생성 시 파라미터 유효성 에러")
    @Test
    void invalidRequest() throws Exception {
        WaterMeterCreateRequest request = WaterMeterCreateRequest.of(10, 20, YearMonth.of(2023, 8));

        List<ValidationError> errors = List.of(
                ValidationError.of("previousMeter", ValidationCode.NEGATIVE.getValue()),
                ValidationError.of("presentMeter", ValidationCode.NEGATIVE.getValue()),
                ValidationError.of("calculateYm", ValidationCode.NULL.getValue())
        );

        doThrow(new ValidationErrorException("Request has invalid values", errors))
                .when(waterMeterCreateService).create(any(), any(WaterMeterCreateRequest.class));

        mockMvc.perform(post("/water-meter")
                        .content(writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }
}
