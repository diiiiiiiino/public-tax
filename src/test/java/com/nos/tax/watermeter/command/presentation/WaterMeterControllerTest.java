package com.nos.tax.watermeter.command.presentation;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.BaseControllerTest;
import com.nos.tax.watermeter.command.application.dto.WaterMeterCreateRequest;
import com.nos.tax.watermeter.command.application.service.WaterMeterCreateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.YearMonth;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class WaterMeterControllerTest extends BaseControllerTest {

    @MockBean
    private WaterMeterCreateService waterMeterCreateService;

    @BeforeEach
    void beforeEach() throws Exception {
        login("abcde", "qwer1234!@");
    }

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

        getResultActions(request, post("/water-meter"))
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andDo(print());
    }
}
