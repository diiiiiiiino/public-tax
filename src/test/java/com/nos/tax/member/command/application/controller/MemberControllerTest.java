package com.nos.tax.member.command.application.controller;

import com.nos.tax.common.exception.ValidationException;
import com.nos.tax.helper.BaseControllerTest;
import com.nos.tax.member.command.application.dto.MemberCreateRequest;
import com.nos.tax.member.command.application.dto.MemberUpdateRequest;
import com.nos.tax.member.command.application.dto.PasswordChangeRequest;
import com.nos.tax.member.command.application.exception.ExpiredInviteCodeException;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.exception.InviteCodeNotFoundException;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.application.service.MemberCreateService;
import com.nos.tax.member.command.application.service.MemberInfoChangeService;
import com.nos.tax.member.command.application.service.PasswordChangeService;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.exception.PasswordNotMatchedException;
import com.nos.tax.member.command.domain.exception.UpdatePasswordSameException;
import com.nos.tax.member.command.presentation.MemberController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
public class MemberControllerTest extends BaseControllerTest {

    @MockBean
    private MemberCreateService memberCreateService;

    @MockBean
    private MemberInfoChangeService memberInfoChangeService;

    @MockBean
    private PasswordChangeService passwordChangeService;

    @DisplayName("회원 정보 수정 시 유효성 에러")
    @Test
    void whenMemberUpdateThenInvalidRequest() throws Exception {
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .name("홍길동")
                .mobile("01012345678")
                .build();

        doThrow(new ValidationException("Has No Text"))
                .when(memberInfoChangeService).change(any(Member.class), any(MemberUpdateRequest.class));

        mockMvc.perform(patch("/member")
                        .content(writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 생성 시 초대코드 미조회")
    @Test
    void inviteCodeNotFound() throws Exception {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", 1L, "123456");

        doThrow(new InviteCodeNotFoundException("not found inviteCode"))
                .when(memberCreateService).create(any(MemberCreateRequest.class));

        mockMvc.perform(post("/member")
                        .content(writeValueAsString(memberCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @DisplayName("회원 생성 시 초대코드 만료")
    @Test
    void inviteCodeExpired() throws Exception {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", 1L, "123456");

        doThrow(new ExpiredInviteCodeException("expired inviteCode"))
                .when(memberCreateService).create(any(MemberCreateRequest.class));

        mockMvc.perform(post("/member")
                        .content(writeValueAsString(memberCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 생성 시 세대 미조회")
    @Test
    void houseHoldNotFound() throws Exception {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", 1L, "123456");

        doThrow(new HouseHoldNotFoundException("not found houseHold"))
                .when(memberCreateService).create(any(MemberCreateRequest.class));

        mockMvc.perform(post("/member")
                        .content(writeValueAsString(memberCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @DisplayName("회원 생성 성공")
    @Test
    void memberCreateSuccess() throws Exception {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", 1L, "123456");

        mockMvc.perform(post("/member")
                        .content(writeValueAsString(memberCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @DisplayName("회원 정보 수정 시 회원 미조회")
    @Test
    void whenMemberUpdateThenMemberNotFound() throws Exception {
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .name("홍길동")
                .mobile("01012345678")
                .build();

        doThrow(new MemberNotFoundException("Member not found"))
                .when(memberInfoChangeService).change(any(Member.class), any(MemberUpdateRequest.class));

        mockMvc.perform(patch("/member")
                .content(writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @DisplayName("회원 비밀번호 수정 시 유효성 에러")
    @Test
    void whenPasswordUpdateThenInvalidRequest() throws Exception {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!!")
                .newPassword("qwer1234@@")
                .build();

        doThrow(new ValidationException("Has No Text"))
                .when(passwordChangeService).change(any(Member.class), any(PasswordChangeRequest.class));

        mockMvc.perform(patch("/member/password")
                        .content(writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 비밀번호 수정 시 회원 미조회")
    @Test
    void whenPasswordUpdateThenMemberNotFound() throws Exception {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!!")
                .newPassword("qwer1234@@")
                .build();

        doThrow(new MemberNotFoundException("Member not found"))
                .when(passwordChangeService).change(any(Member.class), any(PasswordChangeRequest.class));

        mockMvc.perform(patch("/member/password")
                        .content(writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @DisplayName("회원 비밀번호 수정 시 비밀번호가 틀렸을 경우")
    @Test
    void whenPasswordUpdateThenPasswordNotMatched() throws Exception {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!!")
                .newPassword("qwer1234@@")
                .build();

        doThrow(new PasswordNotMatchedException("password is not matched"))
                .when(passwordChangeService).change(any(Member.class), any(PasswordChangeRequest.class));

        mockMvc.perform(patch("/member/password")
                        .content(writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 비밀번호 수정 시 기존 비밀번호와 변경 비밀번호가 같을때")
    @Test
    void whenPasswordUpdateThenPasswordSame() throws Exception {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!!")
                .newPassword("qwer1234@@")
                .build();

        doThrow(new UpdatePasswordSameException("origin and update password same"))
                .when(passwordChangeService).change(any(Member.class), any(PasswordChangeRequest.class));

        mockMvc.perform(patch("/member/password")
                        .content(writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 비밀번호 수정 성공")
    @Test
    void whenPasswordUpdateThenSuccess() throws Exception {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!!")
                .newPassword("qwer1234@@")
                .build();

        mockMvc.perform(patch("/member/password")
                        .content(writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}
