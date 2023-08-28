package com.nos.tax.household.command.presentation.controller;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.BaseControllerTest;
import com.nos.tax.household.command.application.HouseHoldMoveOutService;
import com.nos.tax.household.command.application.HouseHolderChangeService;
import com.nos.tax.household.command.presentaion.HouseHoldController;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HouseHoldController.class)
public class HouseHoldControllerTest extends BaseControllerTest {

    @MockBean
    private HouseHoldMoveOutService houseHoldMoveOutService;

    @MockBean
    private HouseHolderChangeService houseHolderChangeService;

    @DisplayName("세대 이사시 세대 ID NULL인 경우")
    @Test
    void whenHouseHoldMoveOutThenHouseHoldIdIsNull() throws Exception {
        doThrow(new ValidationErrorException("householdId is null"))
                .when(houseHoldMoveOutService).leave(anyLong());

        mockMvc.perform(post("/household/move-out/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("InvalidRequest"));
    }

    @DisplayName("세대 이사시 세대 미조회")
    @Test
    void whenHouseHoldMoveOutThenHouseHoldNotFound() throws Exception {
        doThrow(new HouseHoldNotFoundException("Household not found"))
                .when(houseHoldMoveOutService).leave(anyLong());

        mockMvc.perform(post("/household/move-out/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("HouseHoldNotFound"));
    }

    @DisplayName("세대 이사 성공")
    @Test
    void whenHouseHoldMoveOutThenSuccess() throws Exception {
        mockMvc.perform(post("/household/move-out/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("세대주 변경 시 유효성 체크")
    @Test
    void whenHouseHolderChangeThenInvalidRequest() throws Exception {
        List<ValidationError> errors = new ArrayList<>();
        errors.add(ValidationError.of("houseHoldId", ValidationCode.NULL.getValue()));
        errors.add(ValidationError.of("memberId", ValidationCode.NULL.getValue()));

        doThrow(new ValidationErrorException("Request has invalid values", errors))
                .when(houseHolderChangeService).change(anyLong(), anyLong());

        mockMvc.perform(post("/household/householder/1/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andDo(print());
    }

    @DisplayName("세대주 변경 시 세대 미조회")
    @Test
    void whenHouseHolderChangeThenHouseHoldNotFound() throws Exception {
        doThrow(new HouseHoldNotFoundException("Household not found"))
                .when(houseHolderChangeService).change(anyLong(), anyLong());

        mockMvc.perform(post("/household/householder/1/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("HouseHoldNotFound"));
    }

    @DisplayName("세대주 변경 시 회원 미조회")
    @Test
    void whenHouseHolderChangeThenMemberNotFound() throws Exception {
        doThrow(new MemberNotFoundException("Member not found"))
                .when(houseHolderChangeService).change(anyLong(), anyLong());

        mockMvc.perform(post("/household/householder/1/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("MemberNotFound"));
    }

    @DisplayName("세대주 변경 성공")
    @Test
    void whenHouseHolderChangeThenSuccess() throws Exception {
        mockMvc.perform(post("/household/householder/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
