package com.nos.tax.helper.builder;

import com.nos.tax.authority.command.domain.Authority;
import com.nos.tax.authority.command.domain.enumeration.AuthorityEnum;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.MemberAuthority;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.member.command.domain.Password;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.function.Function;

public class MemberCreateHelperBuilder {
    private Long id;
    private Password password = Password.of("qwer1234!@#$", new BCryptPasswordEncoder());
    private Mobile mobile = Mobile.of("01011112222");
    private String loginId = "loginId";
    private String name = "홍길동";
    private List<Function<Member, MemberAuthority>> functions = List.of(member -> MemberAuthority.of(member, Authority.of(AuthorityEnum.ROLE_MEMBER)));

    public static MemberCreateHelperBuilder builder(){
        return new MemberCreateHelperBuilder();
    }

    public MemberCreateHelperBuilder id(Long id) {
        this.id = id;
        return this;
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

    public MemberCreateHelperBuilder functions(List<Function<Member, MemberAuthority>> functions){
        this.functions = functions;
        return this;
    }

    public Member build(){
        return Member.of(id, loginId, password, name, mobile, functions);
    }
}
