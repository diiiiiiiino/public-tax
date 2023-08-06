package com.nos.tax.member.command.infra;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

public class AlertCreateMemberServiceImplTest {

    private AlertCreateMemberServiceImpl alertCreateMemberServiceImpl;

    public AlertCreateMemberServiceImplTest() {
        alertCreateMemberServiceImpl = new AlertCreateMemberServiceImpl();
    }

    @DisplayName("전화번호가 null 또는 빈 문자열일때")
    @ParameterizedTest
    @NullAndEmptySource
    void mobile_null_and_empty(String mobile) {
        String inviteCode = "123456";

        Assertions.assertThatThrownBy(() -> alertCreateMemberServiceImpl.alert(mobile, inviteCode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Has No Text");
    }

    @DisplayName("초대코드가 null 또는 빈 문자열일때")
    @ParameterizedTest
    @NullAndEmptySource
    void inviteCode_null_and_empty(String inviteCode) {
        String mobile = "01012345678";

        Assertions.assertThatThrownBy(() -> alertCreateMemberServiceImpl.alert(mobile, inviteCode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Has No Text");
    }
}
