package com.nos.tax.login.command.application.presentation;

import com.nos.tax.common.http.Response;
import com.nos.tax.login.command.application.service.LoginRecordService;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.application.security.SecurityMember;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로그인 관련 처리 Controller
 */
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginRecordService loginRecordService;

    /**
     * 로그인 이력 기록
     * @param securityMember 인증 회원
     * @param request
     * @return Response
     * @throws MemberNotFoundException 회원 미조회
     */
    @PostMapping
    public Response<Void> login(@AuthenticationPrincipal SecurityMember securityMember,
                                HttpServletRequest request) {
        loginRecordService.loginRecord(securityMember.getMember(), request.getHeader(HttpHeaders.USER_AGENT));
        return Response.ok();
    }
}
