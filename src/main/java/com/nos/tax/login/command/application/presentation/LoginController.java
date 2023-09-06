package com.nos.tax.login.command.application.presentation;

import com.nos.tax.common.http.Response;
import com.nos.tax.login.command.application.service.LoginService;
import com.nos.tax.member.command.application.security.SecurityMember;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public Response<Void> login(@AuthenticationPrincipal SecurityMember securityMember,
                                HttpServletRequest request) throws IOException {
        loginService.login(securityMember.getMember(), request.getHeader(HttpHeaders.USER_AGENT));
        return Response.ok();
    }
}
