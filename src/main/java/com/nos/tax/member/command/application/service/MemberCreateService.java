package com.nos.tax.member.command.application.service;

import com.nos.tax.common.component.DateUtils;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.validator.RequestValidator;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.HouseHolder;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.invite.command.domain.MemberInvite;
import com.nos.tax.invite.command.domain.repository.MemberInviteCodeRepository;
import com.nos.tax.member.command.application.dto.MemberCreateRequest;
import com.nos.tax.member.command.application.exception.ExpiredInviteCodeException;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.exception.InviteCodeNotFoundException;
import com.nos.tax.member.command.application.validator.annotation.MemberCreateRequestQualifier;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 회원 생성 서비스
 */
@Service
public class MemberCreateService {
    private final DateUtils dateUtils;
    private final PasswordEncoder passwordEncoder;
    private final MemberInviteCodeRepository memberInviteCodeRepository;
    private final HouseHoldRepository houseHoldRepository;
    private final MemberRepository memberRepository;
    private final RequestValidator validator;

    public MemberCreateService(DateUtils dateUtils,
                               PasswordEncoder passwordEncoder,
                               MemberInviteCodeRepository memberInviteCodeRepository,
                               HouseHoldRepository houseHoldRepository,
                               MemberRepository memberRepository,
                               @MemberCreateRequestQualifier RequestValidator validator) {
        this.dateUtils = dateUtils;
        this.passwordEncoder = passwordEncoder;
        this.memberInviteCodeRepository = memberInviteCodeRepository;
        this.houseHoldRepository = houseHoldRepository;
        this.memberRepository = memberRepository;
        this.validator = validator;
    }

    /**
     * 회원 생성
     * @param request 회원 생성 요청
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code loginId}가 {@code null}이거나 문자가 없을 경우, 길이가 1~15 아닌 경우
     *     <li>{@code password}가 {@code null}이거나 문자가 없을 경우, 길이가 8~16 아닌 경우
     *     <li>{@code name}이 {@code null}이거나 문자가 없을 경우, 길이가 1~15 아닌 경우
     *     <li>{@code mobile}이 {@code null}이거나 문자가 없을 경우, 길이가 11자리가 아닌 경우
     *     <li>{@code inviteCode}가 {@code null}이거나 문자가 없을 경우, 길이가 6자리가 아닌 경우
     *     <li>{@code houseHoldId}가 {@code null}인 경우
     * </ul>
     * @throws InviteCodeNotFoundException 초대코드 미조회
     * @throws ExpiredInviteCodeException 초대코드 만료
     * @throws HouseHoldNotFoundException 세대 미조회
     */
    @Transactional
    public void create(MemberCreateRequest request) {
        List<ValidationError> errors = validator.validate(request);
        if(!errors.isEmpty())
            throw new ValidationErrorException("Request has invalid values", errors);

        MemberInvite memberInvite = memberInviteCodeRepository.findByCode(request.getInviteCode())
                .orElseThrow(() -> new InviteCodeNotFoundException("not found inviteCode"));

        if(memberInvite.isExpired(dateUtils.today())){
            throw new ExpiredInviteCodeException("expired inviteCode");
        }

        HouseHold houseHold = houseHoldRepository.findById(request.getHouseholdId())
                .orElseThrow(() -> new HouseHoldNotFoundException("not found household"));

        Member member = MemberCreateRequest.newMember(request, passwordEncoder);
        memberRepository.save(member);

        houseHold.moveInHouse(HouseHolder.of(member));
    }
}
