package com.nos.tax.login.command.application.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nos.tax.common.http.Response;
import com.nos.tax.login.command.application.service.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final ObjectMapper objectMapper;

    @PostMapping
    public Response<Void> login(HttpServletRequest request) throws IOException {
        System.out.println("login success!!");
        return Response.ok();
    }
}
