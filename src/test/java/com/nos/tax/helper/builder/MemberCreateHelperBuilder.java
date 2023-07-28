package com.nos.tax.helper.builder;

import com.nos.tax.member.domain.Member;
import com.nos.tax.member.domain.Mobile;
import com.nos.tax.member.domain.Password;

public class MemberCreateHelperBuilder {
    private Password password = Password.of("qwer1234!@#$");
    private Mobile mobile = Mobile.of("010", "1111", "2222");
    private String loginId = "loginId";
    private String name = "홍길동";
    private Member member = Member.of(loginId, password, "홍길동", mobile);

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
        return Member.of("loginId", password, "홍길동", mobile);
    }
}
