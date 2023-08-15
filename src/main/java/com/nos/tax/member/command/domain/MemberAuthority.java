package com.nos.tax.member.command.domain;

import com.nos.tax.authority.command.domain.Authority;
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

    private MemberAuthority(Member member, Authority authority){
        this.member = member;
        this.authority = authority;
    }

    public static MemberAuthority of(Member member, Authority authority){
        return new MemberAuthority(member, authority);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberAuthority that = (MemberAuthority) o;
        return id.equals(that.id) && member.equals(that.member) && authority.equals(that.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, authority);
    }
}
