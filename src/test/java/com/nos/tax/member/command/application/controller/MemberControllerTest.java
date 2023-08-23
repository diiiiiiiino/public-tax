package com.nos.tax.member.command.application.controller;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.BaseControllerTest;
import com.nos.tax.member.command.application.dto.MemberCreateRequest;
import com.nos.tax.member.command.application.dto.MemberInfoChangeRequest;
import com.nos.tax.member.command.application.dto.PasswordChangeRequest;
import com.nos.tax.member.command.application.exception.ExpiredInviteCodeException;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.exception.InviteCodeNotFoundException;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.application.service.MemberCreateService;
import com.nos.tax.member.command.application.service.MemberInfoChangeService;
import com.nos.tax.member.command.application.service.PasswordChangeService;
import com.nos.tax.member.command.domain.exception.PasswordNotMatchedException;
import com.nos.tax.member.command.domain.exception.PasswordOutOfConditionException;
import com.nos.tax.member.command.domain.exception.UpdatePasswordSameException;
import com.nos.tax.member.command.presentation.MemberController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
public class MemberControllerTest extends BaseControllerTest {

    @MockBean
    private MemberCreateService memberCreateService;

    @MockBean
    private MemberInfoChangeService memberInfoChangeService;

    @MockBean
    private PasswordChangeService passwordChangeService;

    @DisplayName("회원 생성 시 유효성 체크")
    @Test
    void whenMemberCreateThenInvalidRequest() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", 1L, "123456");

        List<ValidationError> errors = new ArrayList<>();
        errors.add(ValidationError.of("memberLoginId", ValidationCode.NO_TEXT.getValue()));
        errors.add(ValidationError.of("memberPassword", ValidationCode.NO_TEXT.getValue()));
        errors.add(ValidationError.of("houseHoldId", ValidationCode.NULL.getValue()));

        doThrow(new ValidationErrorException("Request has invalid values", errors))
                .when(memberCreateService).create(any(MemberCreateRequest.class));

        perform(post("/member"), request, status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andDo(print());
    }

    @DisplayName("회원 생성 시 초대코드 미조회")
    @Test
    void inviteCodeNotFound() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", 1L, "123456");

        doThrow(new InviteCodeNotFoundException("not found inviteCode"))
                .when(memberCreateService).create(any(MemberCreateRequest.class));

        perform(post("/member"), request, status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("InviteCodeNotFound"));
    }

    @DisplayName("회원 생성 시 초대코드 만료")
    @Test
    void inviteCodeExpired() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", 1L, "123456");

        doThrow(new ExpiredInviteCodeException("expired inviteCode"))
                .when(memberCreateService).create(any(MemberCreateRequest.class));

        perform(post("/member"), request, status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("InviteCodeExpired"));
    }

    @DisplayName("회원 생성 시 세대 미조회")
    @Test
    void houseHoldNotFound() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", 1L, "123456");

        doThrow(new HouseHoldNotFoundException("not found houseHold"))
                .when(memberCreateService).create(any(MemberCreateRequest.class));

        perform(post("/member"), request, status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("HouseHoldNotFound"));
    }

    @DisplayName("회원 생성 시 비밀번호 정책에 맞지 않을 때")
    @Test
    void passwordInvalid() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest("loginId", "qwer1234", "홍길동", "01012345678", 1L, "123456");

        doThrow(new PasswordOutOfConditionException("Has no special characters"))
                .when(memberCreateService).create(any(MemberCreateRequest.class));

        perform(post("/member"), request, status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("PasswordOutOfCondition"));
    }

    @DisplayName("회원 생성 성공")
    @Test
    void memberCreateSuccess() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", 1L, "123456");

        perform(post("/member"), request, status().isOk());
    }

    @DisplayName("회원 정보 수정 시 유효성 에러")
    @Test
    void whenMemberUpdateThenInvalidRequest() throws Exception {
        MemberInfoChangeRequest request = MemberInfoChangeRequest.builder()
                .name("홍길동")
                .mobile("01012345678")
                .build();

        List<ValidationError> errors = new ArrayList<>();
        errors.add(ValidationError.of("memberName", ValidationCode.NO_TEXT.getValue()));
        errors.add(ValidationError.of("memberMobile", ValidationCode.NO_TEXT.getValue()));

        doThrow(new ValidationErrorException("Request has invalid values", errors))
                .when(memberInfoChangeService).change(any(), any(MemberInfoChangeRequest.class));

        perform(patch("/member"), request, status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andDo(print());
    }

    @DisplayName("회원 정보 수정 시 회원 미조회")
    @Test
    void whenMemberUpdateThenMemberNotFound() throws Exception {
        MemberInfoChangeRequest request = MemberInfoChangeRequest.builder()
                .name("홍길동")
                .mobile("01012345678")
                .build();

        doThrow(new MemberNotFoundException("Member not found"))
                .when(memberInfoChangeService).change(any(), any(MemberInfoChangeRequest.class));

        perform(patch("/member"), request, status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("MemberNotFound"));
    }

    @DisplayName("회원 비밀번호 수정 시 유효성 에러")
    @Test
    void whenPasswordUpdateThenInvalidRequest() throws Exception {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!!")
                .newPassword("qwer1234@@")
                .build();

        List<ValidationError> errors = new ArrayList<>();
        errors.add(ValidationError.of("memberOrgPassword", ValidationCode.NO_TEXT.getValue()));

        doThrow(new ValidationErrorException("has no text", errors))
                .when(passwordChangeService).change(any(), any(PasswordChangeRequest.class));

        perform(patch("/member/password"), request, status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andDo(print());
    }

    @DisplayName("회원 비밀번호 수정 시 회원 미조회")
    @Test
    void whenPasswordUpdateThenMemberNotFound() throws Exception {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!!")
                .newPassword("qwer1234@@")
                .build();

        doThrow(new MemberNotFoundException("Member not found"))
                .when(passwordChangeService).change(any(), any(PasswordChangeRequest.class));

        perform(patch("/member/password"), request, status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("MemberNotFound"));
    }

    @DisplayName("회원 비밀번호 수정 시 비밀번호가 틀렸을 경우")
    @Test
    void whenPasswordUpdateThenPasswordNotMatched() throws Exception {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!!")
                .newPassword("qwer1234@@")
                .build();

        doThrow(new PasswordNotMatchedException("password is not matched"))
                .when(passwordChangeService).change(any(), any(PasswordChangeRequest.class));

        perform(patch("/member/password"), request, status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("PasswordNotMatched"));
    }

    @DisplayName("회원 비밀번호 수정 시 기존 비밀번호와 변경 비밀번호가 같을때")
    @Test
    void whenPasswordUpdateThenPasswordSame() throws Exception {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!!")
                .newPassword("qwer1234@@")
                .build();

        doThrow(new UpdatePasswordSameException("origin and update password same"))
                .when(passwordChangeService).change(any(), any(PasswordChangeRequest.class));

        perform(patch("/member/password"), request, status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("UpdatePasswordSame"));
    }

    @DisplayName("회원 비밀번호 수정 성공")
    @Test
    void whenPasswordUpdateThenSuccess() throws Exception {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!!")
                .newPassword("qwer1234@@")
                .build();

        perform(patch("/member/password"), request, status().isOk());
    }

    private ResultActions perform(MockHttpServletRequestBuilder requestBuilder, Object request, ResultMatcher matcher) throws Exception {
        return mockMvc.perform(requestBuilder
                        .content(writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(matcher)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
