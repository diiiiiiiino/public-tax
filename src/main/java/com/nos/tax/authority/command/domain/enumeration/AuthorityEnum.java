package com.nos.tax.authority.command.domain.enumeration;

import lombok.Getter;

@Getter
public enum AuthorityEnum {

    ROLE_ADMIN(1L, "ROLE_ADMIN"),
    ROLE_MEMBER(2L, "ROLE_MEMBER");

    private Long id;
    private String name;
    AuthorityEnum(Long id, String name){
        this.id = id;
        this.name = name;
    }
}
