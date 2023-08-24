package com.nos.tax.authority.command.domain;

import com.nos.tax.authority.command.domain.enumeration.AuthorityEnum;
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
public class Authority {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(name = "active_yn")
    private boolean isActive = true;

    /**
     * @param name 회원 권한명
     * @throws ValidationErrorException {@code name}이 빈 문자열이거나 문자가 없을때
     */
    private Authority(String name) {
        setName(name);
    }

    /**
     * @param id   회원 권한 ID
     * @param name 회원 권한명
     * @throws ValidationErrorException {@code name}이 빈 문자열이거나 문자가 없을때
     */
    private Authority(Long id, String name) {
        setId(id);
        setName(name);
    }

    /**
     * @param name 회원 권한명
     * @return 회원 권한
     * @throws ValidationErrorException {@code name}이 빈 문자열이거나 문자가 없을때
     */
    public static Authority of(String name){
        return new Authority(name);
    }

    /**
     * @param authorityEnum 권한 열거형 상수
     * @return 회원 권한
     * @throws ValidationErrorException {@code authorityEnum}이 빈 문자열이거나 문자가 없을때
     */
    public static Authority of(AuthorityEnum authorityEnum){
        return new Authority(authorityEnum.getId(), authorityEnum.getName());
    }

    /**
     * @param name 회원 권한명
     * @throws ValidationErrorException {@code name}이 빈 문자열이거나 문자가 없을때
     */
    private void setName(String name) {
        VerifyUtil.verifyText(name, "authorityName");
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
