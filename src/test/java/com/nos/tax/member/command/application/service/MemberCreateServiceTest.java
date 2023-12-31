package com.nos.tax.member.command.application.service;

import com.nos.tax.authority.command.domain.enumeration.AuthorityEnum;
import com.nos.tax.common.component.DateUtils;
import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.builder.HouseHoldCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.HouseHolder;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.invite.command.domain.MemberInvite;
import com.nos.tax.invite.command.domain.repository.MemberInviteCodeRepository;
import com.nos.tax.member.command.application.dto.MemberCreateRequest;
import com.nos.tax.member.command.application.exception.ExpiredInviteCodeException;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.exception.InviteCodeNotFoundException;
import com.nos.tax.member.command.application.validator.MemberCreateRequestValidator;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.MemberAuthority;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemberCreateServiceTest {

    private DateUtils dateUtils;
    private MemberInviteCodeRepository memberInviteCodeRepository;
    private HouseHoldRepository houseHoldRepository;
    private MemberRepository memberRepository;
    private MemberCreateRequestValidator validator;
    private MemberCreateService memberCreateService;

    public MemberCreateServiceTest() {
        dateUtils = mock(DateUtils.class);
        memberInviteCodeRepository = mock(MemberInviteCodeRepository.class);
        houseHoldRepository = mock(HouseHoldRepository.class);
        memberRepository = mock(MemberRepository.class);
        validator = new MemberCreateRequestValidator();
        memberCreateService = new MemberCreateService(dateUtils, new BCryptPasswordEncoder(), memberInviteCodeRepository, houseHoldRepository, memberRepository, validator);
    }

    @DisplayName("생성 요청 파라미터 유효성 오류")
    @Test
    void requestValueInvalid() {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("", "qwer12!", "홍길동", null, null, "123456");

        assertThatThrownBy(() -> memberCreateService.create(memberCreateRequest))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("Request has invalid values")
                .hasFieldOrPropertyWithValue("errors", List.of(
                        ValidationError.of("memberLoginId", ValidationCode.NO_TEXT.getValue()),
                        ValidationError.of("memberPassword", ValidationCode.LENGTH.getValue()),
                        ValidationError.of("memberMobile", ValidationCode.NO_TEXT.getValue()),
                        ValidationError.of("memberHouseHoldId", ValidationCode.EMPTY.getValue())
                ));
    }

    @DisplayName("초대 코드가 존재하지 않는 경우")
    @Test
    void notFountInviteCode() {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", 1L, "123456");

        when(memberInviteCodeRepository.findByCode(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberCreateService.create(memberCreateRequest))
                .isInstanceOf(InviteCodeNotFoundException.class)
                .hasMessage("not found inviteCode");
    }

    @DisplayName("초대 코드가 만료된 경우")
    @Test
    void expiredInviteCode () {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", 1L,"123456");
        MemberInvite memberInvite = MemberInvite.of(HouseHoldCreateHelperBuilder.builder().build(),
                Mobile.of("01012345678"),
                "123456",
                LocalDateTime.of(2023, 8, 7, 20, 15, 0));

        when(dateUtils.today()).thenReturn(LocalDateTime.of(2023, 8, 7, 20, 15, 1));
        when(memberInviteCodeRepository.findByCode(anyString())).thenReturn(Optional.of(memberInvite));

        assertThatThrownBy(() -> memberCreateService.create(memberCreateRequest))
                .isInstanceOf(ExpiredInviteCodeException.class)
                .hasMessage("expired inviteCode");
    }

    @DisplayName("초대코드에 매핑된 세대가 존재하지 않을 경우")
    @Test
    void notFoundHousehold() {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", 1L,"123456");
        MemberInvite memberInvite = MemberInvite.of(HouseHoldCreateHelperBuilder.builder().build(),
                Mobile.of("01012345678"),
                "123456",
                LocalDateTime.of(2023, 8, 7, 20, 15, 0));

        when(dateUtils.today()).thenReturn(LocalDateTime.of(2023, 8, 7, 20, 15, 0));
        when(memberInviteCodeRepository.findByCode(anyString())).thenReturn(Optional.of(memberInvite));
        when(houseHoldRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberCreateService.create(memberCreateRequest))
                .isInstanceOf(HouseHoldNotFoundException.class)
                .hasMessage("not found household");
    }

    @DisplayName("회원 생성 및 세대에 세대주 업데이트")
    @Test
    void createMemberSuccess() {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", 1L,"123456");
        MemberInvite memberInvite = MemberInvite.of(HouseHoldCreateHelperBuilder.builder().build(),
                Mobile.of("01012345678"),
                "123456",
                LocalDateTime.of(2023, 8, 7, 20, 15, 0));

        HouseHold houseHold = HouseHoldCreateHelperBuilder.builder().build();

        when(dateUtils.today()).thenReturn(LocalDateTime.of(2023, 8, 7, 20, 15, 0));
        when(memberInviteCodeRepository.findByCode(anyString())).thenReturn(Optional.of(memberInvite));
        when(houseHoldRepository.findById(anyLong())).thenReturn(Optional.of(houseHold));

        memberCreateService.create(memberCreateRequest);

        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        BDDMockito.then(memberRepository).should().save(memberCaptor.capture());

        Member savedMember = memberCaptor.getValue();
        assertThat(savedMember.getLoginId()).isEqualTo("loginId");
        assertThat(savedMember.getPassword().match("qwer1234!@", new BCryptPasswordEncoder())).isTrue();
        assertThat(savedMember.getMobile().toString()).isEqualTo("01012345678");
        assertThat(savedMember.getName()).isEqualTo("홍길동");

        Set<String> authoritySet = savedMember.getAuthorities()
                .stream()
                .map(MemberAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertThat(authoritySet).hasSize(1);
        assertThat(authoritySet).containsOnly(AuthorityEnum.ROLE_MEMBER.getName());

        HouseHolder houseHolder = houseHold.getHouseHolder();
        assertThat(houseHolder.getMember().getLoginId()).isEqualTo("loginId");
        assertThat(houseHolder.getMember().passwordMatch("qwer1234!@", new BCryptPasswordEncoder())).isTrue();
        assertThat(houseHolder.getMember().getName()).isEqualTo("홍길동");
        assertThat(houseHolder.getName()).isEqualTo("홍길동");
        assertThat(houseHolder.getMobile().toString()).isEqualTo("01012345678");
    }
}
