package com.nos.tax;

import com.nos.tax.member.domain.Member;
import com.nos.tax.member.domain.Mobile;
import com.nos.tax.member.domain.Password;

public class MemberUtils {
    public static Member createMember(){
        Password password = Password.of("qwer1234!@#$");
        Mobile mobile = Mobile.of("010", "1111", "2222");
        Member member = Member.of("loginId", password, "홍길동", mobile);

        return member;
    }
}
