package com.nos.tax.member.command.domain;

import com.nos.tax.common.entity.BaseEntity;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.member.command.domain.converter.MobileConverter;
import com.nos.tax.member.command.domain.exception.PasswordNotMatchedException;
import com.nos.tax.member.command.domain.exception.UpdatePasswordSameException;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static com.nos.tax.common.enumeration.TextLengthRange.MEMBER_NAME;

/**
 * <p>회원 엔티티</p>
 * <p>모든 메서드와 생성자에서 아래와 같은 경우 {@code CustomIllegalArgumentException}를 발생한다.</p>
 * {@code loginId}가 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 20 아닌 경우 <br>
 * {@code name}가 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 15 아닌 경우 <br>
 * {@code functions}가 {@code null}이거나 빈 리스트인 경우 <br>
 * <p>모든 메서드와 생성자에서 아래와 같은 경우 {@code CustomNullPointerException}를 발생한다.</p>
 * {@code password}가 {@code null}인 경우 <br>
 * {@code mobile}이 {@code null}인 경우
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "member")
    private Set<MemberAuthority> authorities = new HashSet<>();

    private boolean isEnabled = true;

    /**
     * @param loginId 로그인 ID
     * @param password 비밀번호
     * @param name 회원명
     * @param mobile 전화번호
     * @param functions 회원권한에 회원 객체를 주입하기 위한 {@code Function} 리스트
     */
    private Member(String loginId, Password password, String name, Mobile mobile, List<Function<Member, MemberAuthority>> functions) {
        setLoginId(loginId);
        setPassword(password);
        setName(name);
        setMobile(mobile);
        changeAuthority(functions);
    }

    /**
     * @param id 회원 ID
     * @param loginId 로그인 ID
     * @param password 비밀번호
     * @param name 회원명
     * @param mobile 전화번호
     * @param functions 회원권한에 회원 객체를 주입하기 위한 {@code Function} 리스트
     */
    private Member(Long id, String loginId, Password password, String name, Mobile mobile, List<Function<Member, MemberAuthority>> functions) {
        this(loginId, password, name, mobile, functions);
        setId(id);
    }

    /**
     * @param loginId 로그인 ID
     * @param password 비밀번호
     * @param name 회원명
     * @param mobile 전화번호
     * @param functions 회원권한에 회원 객체를 주입하기 위한 {@code Function} 리스트
     */
    public static Member of(String loginId, Password password, String name, Mobile mobile, List<Function<Member, MemberAuthority>> functions) {
        return new Member(loginId, password, name, mobile, functions);
    }

    /**
     * @param id 회원 ID
     * @param loginId 로그인 ID
     * @param password 비밀번호
     * @param name 회원명
     * @param mobile 전화번호
     * @param functions 회원권한에 회원 객체를 주입하기 위한 {@code Function} 리스트
     */
    public static Member of(Long id, String loginId, Password password, String name, Mobile mobile, List<Function<Member, MemberAuthority>> functions) {
        return new Member(id, loginId, password, name, mobile, functions);
    }

    /**
     * @param name 회원명
     */
    public void changeName(String name) {
        setName(name);
    }

    /**
     * 전화번호 변경
     * @param mobile 전화번호
     */
    public void changeMobile(Mobile mobile) {
        setMobile(mobile);
    }

    /**
     * 비밀번호 변경
     * @param originPassword 기존 비밀번호
     * @param updatePassword 수정 비밀번호
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code originPassword}가 {@code null}이거나 문자가 없을 경우
     *     <li>{@code updatePassword}가 {@code null}이거나 문자가 없을 경우
     *     <li>{@code updatePassword}가 길이가 8 ~ 16이 아닐때
     *     <li>{@code updatePassword}가 영어가 포함되어있지 않을때
     *     <li>{@code updatePassword}가 숫자가 포함되어있지 않을때
     *     <li>{@code updatePassword}가 특수문자가 포함되어 있지 않을때
     * </ul>
     */
    public void changePassword(String originPassword, String updatePassword, PasswordEncoder passwordEncoder) {
        VerifyUtil.verifyText(originPassword, "memberOriginPassword");
        VerifyUtil.verifyText(updatePassword, "memberUpdatePassword");

        if(!passwordEncoder.matches(originPassword, this.password.getValue())){
            throw new PasswordNotMatchedException("password is not matched");
        }

        if(passwordEncoder.matches(updatePassword, this.password.getValue())){
            throw new UpdatePasswordSameException("origin and update password same");
        }

        setPassword(Password.of(updatePassword, passwordEncoder));
    }

    /**
     * @param password 비밀번호
     * @return 비밀번호 일치 여부
     */
    public boolean passwordMatch(String password, PasswordEncoder passwordEncoder) {
        return this.password.match(password, passwordEncoder);
    }

    /**
     * 회원 권한 변경
     * @param functions 회원권한에 회원 객체를 주입하기 위한 {@code Function} 리스트
     */
    public void changeAuthority(List<Function<Member, MemberAuthority>> functions){
        VerifyUtil.verifyCollection(functions, "memberAuthorities");

        Set<MemberAuthority> authorities = new HashSet<>();

        for(Function<Member, MemberAuthority> function : functions){
            authorities.add(function.apply(this));
        }

        this.authorities = authorities;
    }

    /**
     * 활성화 여부 변경
     * @param isEnabled 활성화 여부
     */
    public void updateIsEnabled(boolean isEnabled){
        this.isEnabled = isEnabled;
    }

    /**
     * @param id
     */
    private void setId(Long id){
        this.id = id;
    }

    /**
     * @param loginId
     */
    private void setLoginId(String loginId) {
        VerifyUtil.verifyTextLength(loginId, "memberLoginId", MEMBER_NAME.getMin(), MEMBER_NAME.getMax());
        this.loginId = loginId;
    }

    /**
     * @param password
     */
    private void setPassword(Password password){
        this.password = VerifyUtil.verifyNull(password, "memberPassword");
    }

    /**
     * @param name
     */
    private void setName(String name) {
        this.name = VerifyUtil.verifyTextLength(name, "memberName", MEMBER_NAME.getMin(), MEMBER_NAME.getMax());
    }

    /**
     * @param mobile
     */
    private void setMobile(Mobile mobile) {
        this.mobile = VerifyUtil.verifyNull(mobile, "memberMobile");
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
