package com.nos.tax.common.enumeration;

import lombok.Getter;

/**
 * 문자열 길이 범위를 지정한다
 */
@Getter
public enum TextLengthRange {
    /**회원명*/
    MEMBER_NAME(1, 15),
    /**로그인 ID*/
    LOGIN_ID(1, 15),
    /**비밀번호*/
    PASSWORD(8, 16),
    /**세대명*/
    HOUSEHOLD_ROOM(1, 6),
    /**회원 초대코드*/
    MEMBER_INVITE_CODE(6, 6),
    /**전화번호*/
    MOBILE(11, 11),
    /**건물명*/
    BUILDING_NAME(1, 20),
    /**주소1*/
    ADDRESS1(1, 50),
    /**주소2*/
    ADDRESS2(1, 50),
    /**우편번호*/
    ZIP_NO(5, 5),
    /**권한명*/
    AUTHORITY_NAME(1, 20),
    /**로그인 UserAgent*/
    LOGIN_RECORD_USER_AGENT(1, 200);

    private int min;
    private int max;

    TextLengthRange(int min, int max) {
        this.min = min;
        this.max = max;
    }
}
