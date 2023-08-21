package com.nos.tax.common.enumeration;

import lombok.Getter;

@Getter
public enum TextLengthRange {
    MEMBER_NAME(1, 15),
    LOGIN_ID(1, 15),
    PASSWORD(8, 16),
    HOUSEHOLD_ROOM(1, 6),
    MEMBER_INVITE_CODE(6, 6),
    MOBILE(11, 11);

    private int min;
    private int max;

    TextLengthRange(int min, int max) {
        this.min = min;
        this.max = max;
    }
}
