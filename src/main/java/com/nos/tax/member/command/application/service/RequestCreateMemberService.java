package com.nos.tax.member.command.application.service;

import com.nos.tax.common.exception.NotFoundException;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.invite.command.domain.MemberInvite;
import com.nos.tax.invite.command.domain.repository.MemberInviteCodeRepository;
import com.nos.tax.member.command.application.dto.RequestCreateMemberRequest;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.util.VerifyUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 회원 생성 요청 서비스
 */
@Service
@RequiredArgsConstructor
public class RequestCreateMemberService {

    private final HouseHoldRepository houseHoldRepository;
    private final MemberInviteCodeRepository memberInviteCodeRepository;
    private final AlertCreateMemberService alertCreateMemberService;
    
    //todo : 트랜잭션 경계 설정
    /**
     * 회원 생성 요청
     * @param requests 회원 생성 요청 리스트
     * @throws HouseHoldNotFoundException 세대 미조회
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code requests}에 {@code null}이나 빈 문자열이 존재했을때</li>
     *     <li>{@code requests} {@code mobile}의 길이가 11자리가 아닌 요청이 있을때</li>
     * </ul>
     */
    public void request(List<RequestCreateMemberRequest> requests) {
        VerifyUtil.verifyCollection(requests, "requests");

        boolean hasNull = requests.stream()
                .anyMatch(request -> !StringUtils.hasText(request.getMobile()) || Objects.isNull(request.getHouseHoldId()));
        if(hasNull){
            throw new ValidationErrorException("list has null or empty value");
        }

        boolean hasMobileLengthNotEleven = requests.stream()
                .map(RequestCreateMemberRequest::getMobile)
                .anyMatch(mobile -> mobile.length() != 11);
        if(hasMobileLengthNotEleven){
            throw new ValidationErrorException("mobile length is not 11");
        }

        List<Long> householdIds = requests.stream()
                .map(RequestCreateMemberRequest::getHouseHoldId)
                .collect(Collectors.toList());

        List<HouseHold> houseHolds = houseHoldRepository.findAllById(householdIds);

        if(houseHolds.size() != householdIds.size()){
            throw new HouseHoldNotFoundException("household is not found");
        }

        List<MemberInvite> inviteCodes = new ArrayList<>();
        for(int i = 0; i < requests.size(); i++){
            HouseHold houseHold = houseHolds.get(i);
            RequestCreateMemberRequest request = requests.get(i);
            String mobile = request.getMobile();

            inviteCodes.add(MemberInvite.of(houseHold, Mobile.of(mobile), RandomStringUtils.randomNumeric(6), LocalDateTime.now().plusMinutes(3L)));
        }

        memberInviteCodeRepository.saveAll(inviteCodes);
        
        for(MemberInvite inviteCode : inviteCodes){
            alertCreateMemberService.alert(inviteCode.getMobile().toString(), inviteCode.getCode());
        }
    }
}
