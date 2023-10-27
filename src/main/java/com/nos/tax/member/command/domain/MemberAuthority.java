package com.nos.tax.member.command.domain;

import com.nos.tax.authority.command.domain.Authority;
import com.nos.tax.common.entity.BaseEntity;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

/**
 * <p>회원 권한 엔티티</p>
 * <p>모든 메서드와 생성자에서 아래와 같은 경우 {@code CustomNullPointerException}를 발생한다.</p>
 * {@code member}가 {@code null}일 때 <br>
 * {@code authority}가 {@code null}일 때
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAuthority extends BaseEntity implements GrantedAuthority  {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Authority authority;

    /**
     * @param member 회원 객체
     * @param authority 권한 객체
     */
    private MemberAuthority(Member member, Authority authority){
        setMember(member);
        setAuthority(authority);
    }

    /**
     * @param member 회원 객체
     * @param authority 권한 객체
     * @return 회원 권한
     */
    public static MemberAuthority of(Member member, Authority authority){
        return new MemberAuthority(member, authority);
    }

    /**
     * @param member 회원 객체
     */
    private void setMember(Member member) {
        VerifyUtil.verifyNull(member, "member");
        this.member = member;
    }

    /**
     * @param authority 권한 객체
     */
    private void setAuthority(Authority authority) {
        VerifyUtil.verifyNull(authority, "authority");
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority.getName();
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
