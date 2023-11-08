package com.nos.tax.member.command.application.security;

import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.enumeration.MemberState;
import com.nos.tax.member.query.LoginDto;
import com.nos.tax.util.VerifyUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityMember implements UserDetails {

    private final Member member;
    private final Long buildingId;
    private final Long houseHoldId;

    /**
     * @param loginDto 로그인 회원 정보
     * @return 인증완료 회원
     * @throws NullPointerException member가 {@code null}일때
     */
    public static SecurityMember from(LoginDto loginDto){
        VerifyUtil.verifyNull(loginDto, "loginDto");

        return new SecurityMember(loginDto.getMember(), loginDto.getBuildingId(), loginDto.getHouseHoldId());
    }

    public Long getMemberId(){
        return member.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getAuthorities();
    }

    @Override
    public String getPassword() {
        return member.getPassword().getValue();
    }

    @Override
    public String getUsername() {
        return member.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return member.getState() == MemberState.ACTIVATION;
    }
}
