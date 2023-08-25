package com.nos.tax.common.enumeration;

import lombok.Getter;

@Getter
public enum TextLengthRange {
    MEMBER_NAME(1, 15),
    LOGIN_ID(1, 15),
    PASSWORD(8, 16),
    HOUSEHOLD_ROOM(1, 6),
    MEMBER_INVITE_CODE(6, 6),
    MOBILE(11, 11),
    BUILDING_NAME(1, 20),
    ADDRESS1(1, 50),
    ADDRESS2(1, 50),
    ZIP_NO(5, 5),
    AUTHORITY_NAME(1, 20),
    LOGIN_RECORD_USER_AGENT(1, 200);

    private int min;
    private int max;

    TextLengthRange(int min, int max) {
        this.min = min;
        this.max = max;
    }
}
