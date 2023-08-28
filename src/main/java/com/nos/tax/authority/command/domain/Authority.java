package com.nos.tax.authority.command.domain;

import com.nos.tax.authority.command.domain.enumeration.AuthorityEnum;
import com.nos.tax.common.exception.CustomIllegalArgumentException;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.nos.tax.common.enumeration.TextLengthRange.AUTHORITY_NAME;

/**
 * <p>회원 권한 엔티티</p>
 * <p>모든 메서드와 생성자에서 아래와 같은 경우 {@code CustomIllegalArgumentException}를 발생한다.</p>
 * {@code name}이 {@code null}이거나 문자가 없거나 길이가 1 ~ 10이 아닐때
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(name = "active_yn")
    private boolean isActive = true;

    /**
     * @param name 회원 권한명
     */
    private Authority(String name) {
        setName(name);
    }

    /**
     * @param id   회원 권한 ID
     * @param name 회원 권한명
     */
    private Authority(Long id, String name) {
        setId(id);
        setName(name);
    }

    /**
     * @param name 회원 권한명
     * @return 회원 권한
     */
    public static Authority of(String name){
        return new Authority(name);
    }

    /**
     * @param authorityEnum 권한 열거형 상수
     * @return 회원 권한
     */
    public static Authority of(AuthorityEnum authorityEnum){
        return new Authority(authorityEnum.getId(), authorityEnum.getName());
    }

    /**
     * @param name 회원 권한명
     */
    private void setName(String name) {
        VerifyUtil.verifyTextLength(name, "authorityName", AUTHORITY_NAME.getMin(), AUTHORITY_NAME.getMax());
        this.name = name;
    }

    /**
     * @param id 회원 권한 ID
     */
    private void setId(Long id){
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return isActive == authority.isActive && Objects.equals(id, authority.id) && name.equals(authority.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isActive);
    }
}
