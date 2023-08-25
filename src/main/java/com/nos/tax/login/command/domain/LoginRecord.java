package com.nos.tax.login.command.domain;

import com.nos.tax.common.exception.ValidationErrorException;
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

    /**
     * @param member 회원 객체
     * @param loginTime 로그인 일시
     * @param userAgent 유저 에이전트
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code member}가 {@code null}인 경우
     *     <li>{@code loginTime}이 {@code null}인 경우
     *     <li>{@code userAgent}이 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 200이 아닌 경우
     * </ul>
     */
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

    /**
     * @param member 회원 객체
     * @throws ValidationErrorException {@code member}가 {@code null}인 경우
     */
    private void setMember(Member member) {
        this.member = VerifyUtil.verifyNull(member, "loginRecordMember");
    }

    /**
     * @param userAgent 유저 에이전트
     * @throws ValidationErrorException {@code userAgent}이 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 200이 아닌 경우
     */
    private void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * @param loginTime 로그인 일시
     * @throws ValidationErrorException {@code loginTime}이 {@code null}인 경우
     */
    private void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = VerifyUtil.verifyNull(loginTime, "loginRecordLoginTime");
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
}
