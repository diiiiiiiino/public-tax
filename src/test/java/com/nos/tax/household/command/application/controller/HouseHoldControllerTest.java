package com.nos.tax.household.command.application.controller;

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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HouseHoldController.class)
public class HouseHoldControllerTest extends BaseControllerTest {

    @MockBean
    private HouseHoldMoveOutService houseHoldMoveOutService;

    @MockBean
    private HouseHolderChangeService houseHolderChangeService;

    @DisplayName("세대 이사시 세대 미조회")
    @Test
    void whenHouseHoldMoveOutThenHouseHoldNotFound() throws Exception {
        doThrow(new HouseHoldNotFoundException("Household not found"))
                .when(houseHoldMoveOutService).leave(anyLong());

        mockMvc.perform(post("/household/move-out/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("세대 이사 성공")
    @Test
    void whenHouseHoldMoveOutThenSuccess() throws Exception {
        mockMvc.perform(post("/household/move-out/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("세대주 변경 시 세대 미조회")
    @Test
    void whenHouseHolderChangeThenHouseHoldNotFound() throws Exception {
        doThrow(new HouseHoldNotFoundException("Household not found"))
                .when(houseHolderChangeService).change(anyLong(), anyLong());

        mockMvc.perform(post("/household/householder/1/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("errorCode").value("HouseHoldNotFound"));
    }

    @DisplayName("세대주 변경 시 회원 미조회")
    @Test
    void whenHouseHolderChangeThenMemberNotFound() throws Exception {
        doThrow(new MemberNotFoundException("Member not found"))
                .when(houseHolderChangeService).change(anyLong(), anyLong());

        mockMvc.perform(post("/household/householder/1/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("errorCode").value("MemberNotFound"));
    }

    @DisplayName("세대주 변경 성공")
    @Test
    void whenHouseHolderChangeThenSuccess() throws Exception {
        mockMvc.perform(post("/household/householder/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
