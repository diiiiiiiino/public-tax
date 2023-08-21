package com.nos.tax.household.command.domain;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.builder.HouseHolderCreateHelperBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HouseholderTest {

    @DisplayName("세대주 생성 시 이름 누락")
    @ParameterizedTest
    @NullAndEmptySource
    void householder_create_with_null_and_empty_name(String name){
        assertThatThrownBy(() -> HouseHolderCreateHelperBuilder.builder().name(name).build())
                .isInstanceOf(ValidationErrorException.class);
    }

    @DisplayName("세대주 생성 시 Mobile 밸류 누락")
    @Test
    void householder_create_with_null_mobile() {
        assertThatThrownBy(() -> HouseHolderCreateHelperBuilder.builder().mobile(null).build())
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("houseHolderMobile is null");
    }

    @DisplayName("세대주 생성 성공")
    @Test
    void householder_create_success() {
        HouseHolder houseHolder = HouseHolderCreateHelperBuilder.builder().build();

        assertThat(houseHolder.getName()).isEqualTo("세대주");
    }
}
