package com.nos.tax.authority.command.domain.enumeration;

import lombok.Getter;

@Getter
public enum AuthorityEnum {

    ROLE_ADMIN(1L),
    ROLE_MEMBER(2L);

    private Long id;
    AuthorityEnum(Long id){
        this.id = id;
    }
}
