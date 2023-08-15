package com.nos.tax.member.command.domain;

import com.nos.tax.authority.command.domain.Authority;
import com.nos.tax.authority.command.domain.enumeration.AuthorityEnum;
import com.nos.tax.member.command.domain.converter.MobileConverter;
import com.nos.tax.member.command.domain.exception.PasswordChangeException;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String loginId;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "password"))
    })
    @Column(nullable = false)
    private Password password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Convert(converter = MobileConverter.class)
    private Mobile mobile;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "member")
    private Set<MemberAuthority> authorities = new HashSet<>();

    private Member(String loginId, Password password, String name, Mobile mobile, List<Function<Member, MemberAuthority>> functions) {
        setLoginId(loginId);
        setPassword(password);
        setName(name);
        setMobile(mobile);
        addAuthority(functions);
    }

    private Member(Long id, String loginId, Password password, String name, Mobile mobile, List<Function<Member, MemberAuthority>> functions) {
        this(loginId, password, name, mobile, functions);
        setId(id);
    }

    public static Member of(String loginId, Password password, String name, Mobile mobile, List<Function<Member, MemberAuthority>> functions) {
        return new Member(loginId, password, name, mobile, functions);
    }

    public static Member of(Long id, String loginId, Password password, String name, Mobile mobile, List<Function<Member, MemberAuthority>> functions) {
        return new Member(id, loginId, password, name, mobile, functions);
    }

    public void changeName(String name) {
        setName(name);
    }

    public void changeMobile(Mobile mobile) {
        setMobile(mobile);
    }

    public void changePassword(String originPassword, String updatePassword) {
        VerifyUtil.verifyText(originPassword);

        if(!this.password.match(originPassword)){
            throw new PasswordChangeException("password is not the same");
        }

        if(this.password.match(updatePassword)){
            throw new PasswordChangeException("origin and update password same");
        }

        setPassword(Password.of(updatePassword));
    }

    public boolean passwordMatch(String password) {
        return this.password.match(password);
    }

    public void addAuthority(List<Function<Member, MemberAuthority>> functions){
        VerifyUtil.verifyCollection(functions);

        Set<MemberAuthority> authorities = new HashSet<>();

        for(Function<Member, MemberAuthority> function : functions){
            authorities.add(function.apply(this));
        }

        this.authorities.addAll(authorities);
    }

    private void setId(Long id){
        this.id = id;
    }

    private void setLoginId(String loginId) {
        this.loginId = VerifyUtil.verifyText(loginId);
    }

    private void setPassword(Password password){
        this.password = Objects.requireNonNull(password);
    }

    private void setName(String name) {
        this.name = VerifyUtil.verifyText(name);
    }

    private void setMobile(Mobile mobile) {
        this.mobile = Objects.requireNonNull(mobile);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
