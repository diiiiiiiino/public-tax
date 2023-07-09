package com.nos.tax.member;

import com.nos.tax.member.domain.Mobile;
import com.nos.tax.member.domain.MobileNum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MemberAggregationTest {

    @DisplayName("상세 전화번호 생성 시 설정 길이가 너무 짧거나 길 때 실패")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 6, 10})
    void givenTooLongLengthWhenMobileNumCreateThenIllegalArgumentException(int length) {
        assertThatThrownBy(() -> MobileNum.of("010", length))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("MobileNum Length Invalid");
    }

    @DisplayName("상세 전화번호 생성 시 설정 길이와 번호가 다를때 실패")
    @Test
    void givenNotMatchLengthAndTextWhenMobileNumCreateThenIllegalArgumentException() {
        assertThatThrownBy(() -> MobileNum.of("010", 4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Length and Num Text Not Matched");
    }

    @DisplayName("상세 전화번호 생성 성공")
    @Test
    void WhenMobileNumCreateThenSuccess() {
        MobileNum mobileNum = MobileNum.of("010", 3);

        assertThat(mobileNum).isNotNull();
        assertThat(mobileNum.getNum()).isEqualTo("010");
        assertThat(mobileNum.getLength()).isEqualTo(3);
    }

    @DisplayName("전화번호 생성 시 null 또는 빈 문자열 전달 시 실패")
    @ParameterizedTest
    @MethodSource("provideMobileArguments")
    void whenMobileCreateThenIllegalArgumentException(String carrierNum, String secondNum, String threeNum) {
        assertThatThrownBy(() -> Mobile.of(carrierNum, secondNum, threeNum))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전화번호 생성 시 첫번째 번호가 정해진 길이와 다를 경우")
    @ParameterizedTest
    @ValueSource(strings = {"0", "01", "0100"})
    void givenTooLongFirstNumWhenMobileCreateThenIllegalArgumentException(String carrierNum) {
        assertThatThrownBy(() -> Mobile.of(carrierNum, "1111", "2222"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Length and Num Text Not Matched");
    }

    @DisplayName("전화번호 생성 성공")
    @Test
    void whenMobileCreateThenSuccess() {
        Mobile mobile = Mobile.of("010", "1111", "2222");

        assertThat(mobile).isNotNull();
        assertThat(mobile.toString()).isEqualTo("010-1111-2222");
    }

    @DisplayName("회원  변경 시 ")
    @Test
    void whenMemberNameChangeThenIllegalArgumentException() {

    }

    private static Stream<Arguments> provideMobileArguments(){
        return Stream.of(
                Arguments.of("", "1111", "2222"),
                Arguments.of(null, "1111", "2222"),
                Arguments.of("010", "", "2222"),
                Arguments.of("010", null, "2222"),
                Arguments.of("010", "1111", ""),
                Arguments.of("010", "1111", null)
        );
    }
}
