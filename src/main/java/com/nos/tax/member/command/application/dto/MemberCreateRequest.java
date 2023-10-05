package com.nos.tax.member.command.application.dto;

import com.nos.tax.authority.command.domain.Authority;
import com.nos.tax.authority.command.domain.enumeration.AuthorityEnum;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.MemberAuthority;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.member.command.domain.Password;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.function.Function;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreateRequest {
    private String loginId;
    private String password;
    private String name;
    private String mobile;
    private Long householdId;
    private String inviteCode;

    public static MemberCreateRequest of(String loginId, String password, String name, String mobile, Long householdId, String inviteCode){
        return new MemberCreateRequest(loginId, password, name, mobile, householdId, inviteCode);
    }

    public static Member newMember(MemberCreateRequest request, PasswordEncoder passwordEncoder){
        List<Function<Member, MemberAuthority>> functions = List.of(member -> MemberAuthority.of(member, Authority.of(AuthorityEnum.ROLE_MEMBER)));
        return Member.of(request.getLoginId(), Password.of(request.getPassword(), passwordEncoder), request.getName(), Mobile.of(request.getMobile()), functions);
    }

    public static Member newAdmin(MemberCreateRequest request, PasswordEncoder passwordEncoder){
        List<Function<Member, MemberAuthority>> functions = List.of(
                member -> MemberAuthority.of(member, Authority.of(AuthorityEnum.ROLE_ADMIN)),
                member -> MemberAuthority.of(member, Authority.of(AuthorityEnum.ROLE_MEMBER)));
        return Member.of(request.getLoginId(), Password.of(request.getPassword(), passwordEncoder), request.getName(), Mobile.of(request.getMobile()), functions);
    }
}
