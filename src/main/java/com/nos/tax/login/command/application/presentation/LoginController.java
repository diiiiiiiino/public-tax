package com.nos.tax.login.command.application.presentation;

import com.nos.tax.common.http.Response;
import com.nos.tax.member.command.application.security.SecurityMember;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    @PostMapping
    public Response<Void> login(@AuthenticationPrincipal SecurityMember securityMember) throws IOException {
        System.out.println("login success!!" + securityMember.getUsername());
        return Response.ok();
    }
}
