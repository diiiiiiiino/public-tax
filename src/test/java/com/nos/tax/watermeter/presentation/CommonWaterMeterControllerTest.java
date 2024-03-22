package com.nos.tax.watermeter.presentation;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.BaseControllerTest;
import com.nos.tax.watermeter.command.application.dto.WaterMeterCreateRequest;
import com.nos.tax.watermeter.command.application.exception.WaterMeterDeleteStateException;
import com.nos.tax.watermeter.command.application.exception.WaterMeterNotFoundException;
import com.nos.tax.watermeter.command.application.service.WaterMeterCreateService;
import com.nos.tax.watermeter.command.application.service.WaterMeterDeleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.YearMonth;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class CommonWaterMeterControllerTest extends BaseControllerTest {

    @MockBean
    private WaterMeterCreateService waterMeterCreateService;

    @MockBean
    private WaterMeterDeleteService waterMeterDeleteService;

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

        mvcPerform(post("/common/water-meter"), request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @DisplayName("수도계량 데이터 삭제 시 미조회")
    @Test
    void whenWaterMeterDeleteThenNotFound() throws Exception {
        doThrow(new WaterMeterNotFoundException("WaterMeter not found"))
                .when(waterMeterDeleteService).delete(anyLong());

        mvcPerform(delete("/common/water-meter/1"), null)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("WaterMeterNotFound"));
    }

    @DisplayName("수도 계량 삭제 시 수도 요금 정산 중일때")
    @Test
    void whenWaterMeterDeleteThenWaterBillProcessing() throws Exception {
        doThrow(new WaterMeterDeleteStateException("WaterMeter can be deleted before the water bill is settled"))
                .when(waterMeterDeleteService).delete(anyLong());

        mvcPerform(delete("/common/water-meter/1"), null)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("WaterMeterDeleteState"));
    }

    @DisplayName("수도 계량 삭제 성공")
    @Test
    void whenWaterMeterDeleteSuccess() throws Exception {
        mvcPerform(delete("/common/water-meter/1"), null)
                .andExpect(status().isOk());
    }
}
