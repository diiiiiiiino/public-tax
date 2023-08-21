package com.nos.tax.authority.command.domain;

import com.nos.tax.authority.command.domain.enumeration.AuthorityEnum;
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

    private Authority(String name) {
        setName(name);
    }

    private Authority(Long id, String name) {
        setId(id);
        setName(name);
    }

    public static Authority of(String name){
        return new Authority(name);
    }
    public static Authority of(AuthorityEnum authorityEnum){
        return new Authority(authorityEnum.getId(), authorityEnum.getName());
    }

    private void setName(String name) {
        VerifyUtil.verifyText(name, "authorityName");
        this.name = name;
    }

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
