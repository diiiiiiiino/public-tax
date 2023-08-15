package com.nos.tax.authority.command.domain;

import com.nos.tax.authority.command.domain.enumeration.AuthorityEnum;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "active_yn")
    private boolean isActive;

    private Authority(String name) {
        setName(name);
    }

    private Authority(Long id){
        setId(id);
    }

    public static Authority of(String name){
        return new Authority(name);
    }
    public static Authority of(AuthorityEnum authorityEnum){
        return new Authority(authorityEnum.getId());
    }

    private void setName(String name) {
        VerifyUtil.verifyText(name);
        this.name = name;
    }

    private void setId(Long id){
        this.id = id;
    }
}
