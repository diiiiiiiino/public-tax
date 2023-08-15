package com.nos.tax.member.command.domain;

import com.nos.tax.authority.command.domain.Authority;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
