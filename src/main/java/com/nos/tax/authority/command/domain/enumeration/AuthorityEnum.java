package com.nos.tax.authority.command.domain.enumeration;

import lombok.Getter;

/**
 * 회원 권한
 */
@Getter
public enum AuthorityEnum {

    /**관리자*/
    ROLE_ADMIN(1L, "ROLE_ADMIN"),
    /**일반 회원*/
    ROLE_MEMBER(2L, "ROLE_MEMBER");

    /**
     * authority 테이블의 ID
     */
    private Long id;
    /**
     * authority 테이블의 name
     */
    private String name;
    AuthorityEnum(Long id, String name){
        this.id = id;
        this.name = name;
    }
}
