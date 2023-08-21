package com.nos.tax.login.command.domain;

import com.nos.tax.member.command.domain.Member;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    Member member;

    @Column(nullable = false)
    LocalDateTime loginTime;

    String userAgent;

    private LoginRecord(Member member, LocalDateTime loginTime, String userAgent) {
        setMember(member);
        setLoginTime(loginTime);
        setUserAgent(userAgent);
    }

    public static class LoginRecordBuilder {
        private Member member;
        private LocalDateTime loginTime;
        private String userAgent;

        public LoginRecordBuilder(Member member, LocalDateTime loginTime) {
            this.member = member;
            this.loginTime = loginTime;
        }

        public LoginRecord.LoginRecordBuilder userAgent(String userAgent){
            this.userAgent = userAgent;
            return this;
        }

        public LoginRecord build() {
            return new LoginRecord(this.member, this.loginTime, this.userAgent);
        }

        public String toString() {
            return "LoginRecord.LoginRecordBuilder(member=" + this.member + ", loginTime=" + this.loginTime + ")";
        }
    }

    public static LoginRecordBuilder builder(Member member, LocalDateTime loginTime){
        return new LoginRecordBuilder(member, loginTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginRecord that = (LoginRecord) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private void setMember(Member member) {
        this.member = VerifyUtil.verifyNull(member, "loginRecordMember");
    }

    private void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    private void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = VerifyUtil.verifyNull(loginTime, "loginRecordLoginTime");
    }
}
