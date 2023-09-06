package com.nos.tax.login.command.application.service;

import com.nos.tax.member.command.domain.Member;

public interface LoginService {
    void login(Member member, String userAgent);
}
