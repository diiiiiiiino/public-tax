package com.nos.tax.member.command.domain;

import com.nos.tax.authority.command.domain.Authority;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAuthority {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Authority authority;

    /**
     * @param member 회원 객체
     * @param authority 권한 객체
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code member}가 {@code null}일 때
     *     <li>{@code authority}가 {@code null}일 때
     * </ul>
     */
    private MemberAuthority(Member member, Authority authority){
        setMember(member);
        setAuthority(authority);
    }

    /**
     * @param member 회원 객체
     * @param authority 권한 객체
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code member}가 {@code null}일 때
     *     <li>{@code authority}가 {@code null}일 때
     * </ul>
     * @return 회원 권한
     */
    public static MemberAuthority of(Member member, Authority authority){
        return new MemberAuthority(member, authority);
    }

    /**
     * @param member 회원 객체
     * @throws ValidationErrorException {@code member}가 {@code null}일 때 
     */
    private void setMember(Member member) {
        VerifyUtil.verifyNull(member, "member");
        this.member = member;
    }

    /**
     * @param authority 권한 객체
     * @throws ValidationErrorException {@code authority}가 {@code null}일 때
     */
    private void setAuthority(Authority authority) {
        VerifyUtil.verifyNull(authority, "authority");
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberAuthority that = (MemberAuthority) o;
        return Objects.equals(id, that.id) && Objects.equals(member, that.member) && Objects.equals(authority, that.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, authority);
    }
}
