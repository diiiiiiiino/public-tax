package com.nos.tax.member.command.application.service;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.building.command.domain.repository.BuildingRepository;
import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.member.command.application.dto.AdminCreateRequest;
import com.nos.tax.member.command.application.dto.BuildingInfo;
import com.nos.tax.member.command.application.dto.HouseHoldInfo;
import com.nos.tax.member.command.application.dto.MemberCreateRequest;
import com.nos.tax.member.command.application.validator.AdminCreateRequestValidator;
import com.nos.tax.member.command.application.validator.MemberCreateRequestValidator;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class AdminCreateServiceTest {

    private BuildingRepository buildingRepository;
    private MemberRepository memberRepository;
    private AdminCreateService adminCreateService;
    private AdminCreateRequestValidator validator;
    private MemberCreateRequestValidator memberCreateRequestValidator;

    public AdminCreateServiceTest() {
        buildingRepository = mock(BuildingRepository.class);
        memberRepository = mock(MemberRepository.class);
        memberCreateRequestValidator = new MemberCreateRequestValidator();
        validator = new AdminCreateRequestValidator(memberCreateRequestValidator);
        adminCreateService = new AdminCreateService(buildingRepository, memberRepository, new BCryptPasswordEncoder(), validator);
    }

    @DisplayName("관리자 정보가 없는 경우")
    @Test
    void createAdminWithMissMemberCreateRequest() {
        BuildingInfo buildingInfo = BuildingInfo.of("광동빌라", "서울특별시 강남구 대치동", "광동빌라 A동", "12345");
        List<HouseHoldInfo> houseHoldInfos = List.of(HouseHoldInfo.of("101호", false), HouseHoldInfo.of("102호", true), HouseHoldInfo.of("103호", false), HouseHoldInfo.of("104호", false));

        AdminCreateRequest adminCreateRequest = AdminCreateRequest.of(null, buildingInfo, houseHoldInfos);

        assertThatThrownBy(() -> adminCreateService.create(adminCreateRequest))
                .hasMessage("Request has invalid values")
                .hasFieldOrPropertyWithValue("errors", List.of(
                        ValidationError.of("request", ValidationCode.NULL.getValue())
                ));
    }

    @DisplayName("건물 정보가 없는 경우")
    @Test
    void createAdminWithMissBuildingInfo() {
        MemberCreateRequest memberCreateRequest = MemberCreateRequest.of("loginId", "qwer1234!@", "홍길동", "01012345678", 1L,"123456");
        List<HouseHoldInfo> houseHoldInfos = List.of(HouseHoldInfo.of("101호", false), HouseHoldInfo.of("102호", true), HouseHoldInfo.of("103호", false), HouseHoldInfo.of("104호", false));

        AdminCreateRequest adminCreateRequest = AdminCreateRequest.of(memberCreateRequest, null, houseHoldInfos);

        assertThatThrownBy(() -> adminCreateService.create(adminCreateRequest))
                .hasMessage("Request has invalid values")
                .hasFieldOrPropertyWithValue("errors", List.of(
                        ValidationError.of("buildingInfo", ValidationCode.NULL.getValue())
                ));
    }

    @DisplayName("세대 목록이 null 또는 비어있는 경우")
    @ParameterizedTest
    @NullAndEmptySource
    void createAdminWithNullAndEmptyHouseholds(List<HouseHoldInfo> houseHoldInfos) {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", 1L,"123456");
        BuildingInfo buildingInfo = BuildingInfo.of("광동빌라", "서울특별시 강남구 대치동", "광동빌라 A동", "12345");

        AdminCreateRequest adminCreateRequest = AdminCreateRequest.of(memberCreateRequest, buildingInfo, houseHoldInfos);

        assertThatThrownBy(() -> adminCreateService.create(adminCreateRequest))
                .hasMessage("Request has invalid values")
                .hasFieldOrPropertyWithValue("errors", List.of(
                        ValidationError.of("houseHoldInfos", ValidationCode.EMPTY.getValue())
                ));
    }

    @DisplayName("관리자의 세대 선택이 없거나 1개 이상 있을 때")
    @ParameterizedTest
    @MethodSource("HouseHoldInfosMethodSource")
    void createAdminWithSelectHouseHoldNoneAndOneMore(List<HouseHoldInfo> houseHoldInfos) {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", 1L,"123456");
        BuildingInfo buildingInfo = BuildingInfo.of("광동빌라", "서울특별시 강남구 대치동", "광동빌라 A동", "12345");

        AdminCreateRequest adminCreateRequest = AdminCreateRequest.of(memberCreateRequest, buildingInfo, houseHoldInfos);

        assertThatThrownBy(() -> adminCreateService.create(adminCreateRequest))
                .hasMessage("Request has invalid values")
                .hasFieldOrPropertyWithValue("errors", List.of(
                        ValidationError.of("houseHoldInfos", ValidationCode.SELECT_ONE.getValue())
                ));
    }

    private static Stream<Arguments> HouseHoldInfosMethodSource(){
        return Stream.of(
                Arguments.of(List.of(HouseHoldInfo.of("101호", false), HouseHoldInfo.of("102호", false))),
                Arguments.of(List.of(HouseHoldInfo.of("101호", true), HouseHoldInfo.of("102호", true)))
        );
    }

    @DisplayName("관리자 및 건물/세대 생성")
    @Test
    void createAdminSuccess() {
        MemberCreateRequest memberCreateRequest = MemberCreateRequest.of("loginId", "qwer1234!@", "홍길동", "01012345678", 1L,"123456");
        BuildingInfo buildingInfo = BuildingInfo.of("광동빌라", "서울특별시 강남구 대치동", "광동빌라 A동", "12345");
        List<HouseHoldInfo> houseHoldInfos = List.of(HouseHoldInfo.of("101호", false), HouseHoldInfo.of("102호", true), HouseHoldInfo.of("103호", false), HouseHoldInfo.of("104호", false));

        AdminCreateRequest adminCreateRequest = AdminCreateRequest.of(memberCreateRequest, buildingInfo, houseHoldInfos);

        adminCreateService.create(adminCreateRequest);

        ArgumentCaptor<Building> buildingCaptor = ArgumentCaptor.forClass(Building.class);
        BDDMockito.then(buildingRepository).should().save(buildingCaptor.capture());

        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        BDDMockito.then(memberRepository).should().save(memberCaptor.capture());

        Building savedBuilding = buildingCaptor.getValue();
        Member savedMember = memberCaptor.getValue();

        assertThat(savedBuilding.getName()).isEqualTo("광동빌라");
        assertThat(savedBuilding.getAddress().getAddress1()).isEqualTo("서울특별시 강남구 대치동");
        assertThat(savedBuilding.getAddress().getAddress2()).isEqualTo("광동빌라 A동");
        assertThat(savedBuilding.getAddress().getZipNo()).isEqualTo("12345");
        assertThat(savedBuilding.getHouseHolds()
                .stream()
                .filter(houseHold -> !houseHold.getMembers().isEmpty())
                .count()).isEqualTo(1);
        assertThat(savedMember.getLoginId()).isEqualTo("loginId");
        assertThat(savedMember.getPassword().match("qwer1234!@", new BCryptPasswordEncoder())).isTrue();
        assertThat(savedMember.getMobile().toString()).isEqualTo("01012345678");
        assertThat(savedMember.getName()).isEqualTo("홍길동");
    }
}
