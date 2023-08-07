package com.nos.tax.helper.builder;

import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.member.command.domain.Password;

public class MemberCreateHelperBuilder {
    private Password password = Password.of("qwer1234!@#$");
    private Mobile mobile = Mobile.of("01011112222");
    private String loginId = "loginId";
    private String name = "홍길동";

    public static MemberCreateHelperBuilder builder(){
        return new MemberCreateHelperBuilder();
    }

    public MemberCreateHelperBuilder loginId(String loginId){
        this.loginId = loginId;
        return this;
    }

    public MemberCreateHelperBuilder password(Password password){
        this.password = password;
        return this;
    }

    public MemberCreateHelperBuilder name(String name){
        this.name = name;
        return this;
    }

    public MemberCreateHelperBuilder mobile(Mobile mobile){
        this.mobile = mobile;
        return this;
    }

    public Member build(){
        return Member.of(loginId, password, name, mobile);
    }
}
