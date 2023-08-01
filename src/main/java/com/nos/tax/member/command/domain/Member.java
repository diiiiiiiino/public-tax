package com.nos.tax.member.command.domain;

import com.nos.tax.member.command.domain.converter.MobileConverter;
import com.nos.tax.member.command.domain.exception.PasswordChangeException;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    private Member(String loginId, Password password, String name, Mobile mobile) {
        setLoginId(loginId);
        setPassword(password);
        setName(name);
        setMobile(mobile);
    }

    public static Member of(String loginId, Password password, String name, Mobile mobile) {
        return new Member(loginId, password, name, mobile);
    }

    public void changeName(String name) {
        setName(name);
    }

    public void changeMobile(String carrierNum, String secondNum, String threeNum) {
        setMobile(Mobile.of(carrierNum, secondNum, threeNum));
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
