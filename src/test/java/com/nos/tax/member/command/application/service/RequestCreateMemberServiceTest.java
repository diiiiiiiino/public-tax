package com.nos.tax.member.command.application.service;

import com.nos.tax.common.exception.NotFoundException;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.builder.HouseHoldCreateHelperBuilder;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.invite.command.domain.repository.MemberInviteCodeRepository;
import com.nos.tax.member.command.application.dto.RequestCreateMemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RequestCreateMemberServiceTest {

    private HouseHoldRepository houseHoldRepository;
    private MemberInviteCodeRepository memberInviteCodeRepository;
    private AlertCreateMemberService alertCreateMemberService;
    private RequestCreateMemberService requestCreateMemberService;

    public RequestCreateMemberServiceTest() {
        houseHoldRepository = mock(HouseHoldRepository.class);
        memberInviteCodeRepository = mock(MemberInviteCodeRepository.class);
        alertCreateMemberService = mock(AlertCreateMemberService.class);
        requestCreateMemberService = new RequestCreateMemberService(houseHoldRepository, memberInviteCodeRepository, alertCreateMemberService);
    }

    @DisplayName("회원 생성 요청 목록이 null 또는 비어있을 때")
    @ParameterizedTest
    @NullAndEmptySource
    void requestNullAndEmpty(List<RequestCreateMemberRequest> requests) {
        assertThatThrownBy(() -> requestCreateMemberService.request(requests))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("requests no element");
    }

    @DisplayName("회원 생성 요청에 null 또는 빈 문자열이 포함되어 있을 때")
    @ParameterizedTest
    @MethodSource("nullAndEmptyCreateMemberRequestList")
    void includeNullAndEmptyValue(List<RequestCreateMemberRequest> requests) {
        assertThatThrownBy(() -> requestCreateMemberService.request(requests))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("list has null or empty value");
    }

    @DisplayName("회원 생성 요청에 전화번호 길이가 11자리가 아닐경우")
    @ParameterizedTest
    @MethodSource("mobileLengthNotElevenList")
    void mobileLengthIsNotEleven(List<RequestCreateMemberRequest> requests) {
        assertThatThrownBy(() -> requestCreateMemberService.request(requests))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("mobile length is not 11");
    }

    @DisplayName("회원 생성 요청에 등록되지 않은 세대 ID가 포함되어 있을 때")
    @Test
    void householdIdIsNotFound() {
        List<RequestCreateMemberRequest> requests = List.of(RequestCreateMemberRequest.of("01044445555", 1L), RequestCreateMemberRequest.of("01012345678", 2L));

        when(houseHoldRepository.findAllById(anyList())).thenReturn(List.of());

        assertThatThrownBy(() -> requestCreateMemberService.request(requests))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("household is not found");
    }

    @DisplayName("회원 생성 요청")
    @Test
    void requestCreateMemberSuccess() {
        List<RequestCreateMemberRequest> requests = List.of(RequestCreateMemberRequest.of("01044445555", 1L), RequestCreateMemberRequest.of("01012345678", 2L));

        when(houseHoldRepository.findAllById(any())).thenReturn(List.of(
                HouseHoldCreateHelperBuilder.builder().id(1L).room("101호").build(),
                HouseHoldCreateHelperBuilder.builder().id(2L).room("201호").build()));

        requestCreateMemberService.request(requests);

        verify(alertCreateMemberService, times(2)).alert(anyString(), anyString());
    }

    private static Stream<Arguments> nullAndEmptyCreateMemberRequestList(){
        return Stream.of(
                Arguments.of(List.of(RequestCreateMemberRequest.of("01012345678", 1L), RequestCreateMemberRequest.of(" ", 2L))),
                Arguments.of(List.of(RequestCreateMemberRequest.of("01012345678", 1L), RequestCreateMemberRequest.of(null, 2L))),
                Arguments.of(List.of(RequestCreateMemberRequest.of("01012345678", 1L), RequestCreateMemberRequest.of("", 2L))),
                Arguments.of(List.of(RequestCreateMemberRequest.of("01012345678", 1L), RequestCreateMemberRequest.of("01023456789", null)))
        );
    }

    private static Stream<Arguments> mobileLengthNotElevenList(){
        return Stream.of(
                Arguments.of(List.of(RequestCreateMemberRequest.of("01012345678", 1L), RequestCreateMemberRequest.of("0101234567", 2L))),
                Arguments.of(List.of(RequestCreateMemberRequest.of("01012345678", 1L), RequestCreateMemberRequest.of("010", 2L))),
                Arguments.of(List.of(RequestCreateMemberRequest.of("01012345678", 1L), RequestCreateMemberRequest.of("0", 2L)))
        );
    }
}
