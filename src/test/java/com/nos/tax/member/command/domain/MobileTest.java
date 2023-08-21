package com.nos.tax.member.command.domain;

import com.nos.tax.common.exception.ValidationErrorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MobileTest {

    @DisplayName("전화번호 생성 시 null 또는 빈 문자열 전달 시 실패")
    @ParameterizedTest
    @NullAndEmptySource
    void mobile_create_with_null_and_empty_num(String value) {
        assertThatThrownBy(() -> Mobile.of(value))
                .isInstanceOf(ValidationErrorException.class);
    }

    @DisplayName("전화번호 생성 시 첫번째 번호가 정해진 길이와 다를 경우")
    @ParameterizedTest
    @ValueSource(strings = {"0101111111", "010"})
    void mobile_create_different_firstNum_and_length(String value) {
        assertThatThrownBy(() -> Mobile.of(value))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("mobile length is different set length");
    }

    @DisplayName("전화번호 생성 성공")
    @Test
    void mobile_create_success() {
        Mobile mobile = Mobile.of("01011112222");

        assertThat(mobile).isNotNull();
        assertThat(mobile.toString()).isEqualTo("01011112222");
    }
}
