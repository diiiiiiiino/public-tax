package com.nos.tax.login.presentation;

import com.nos.tax.helper.BaseControllerTest;
import com.nos.tax.login.command.application.service.LoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class LoginControllerTest extends BaseControllerTest {

    @DisplayName("로그인 실패")
    @Test
    void loginFail() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .loginId("abcde")
                .password("qwer12345")
                .build();

        mockMvc.perform(post("/login")
                        .content(writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("로그인 성공")
    @Test
    void loginSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .loginId("abcde")
                .password("qwer1234!@")
                .build();

        mockMvc.perform(post("/login")
                        .content(writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }
}
