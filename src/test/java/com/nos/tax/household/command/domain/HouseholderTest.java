package com.nos.tax.household.command.domain;

import com.nos.tax.helper.builder.HouseHolderCreateHelperBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HouseholderTest {

    @DisplayName("세대주 생성 성공")
    @Test
    void householderCreateSuccess() {
        HouseHolder houseHolder = HouseHolderCreateHelperBuilder.builder().build();

        assertThat(houseHolder.getName()).isEqualTo("홍길동");
    }
}
