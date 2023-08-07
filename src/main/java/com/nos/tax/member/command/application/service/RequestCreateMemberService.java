package com.nos.tax.member.command.application.service;

import com.nos.tax.application.exception.NotFoundException;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.invite.command.domain.MemberInvite;
import com.nos.tax.invite.command.domain.repository.MemberInviteCodeRepository;
import com.nos.tax.member.command.application.dto.CreateMemberRequest;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.util.VerifyUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestCreateMemberService {

    private final HouseHoldRepository houseHoldRepository;
    private final MemberInviteCodeRepository memberInviteCodeRepository;
    private final AlertCreateMemberService alertCreateMemberService;

    public void request(List<CreateMemberRequest> requests) {
        VerifyUtil.verifyList(requests);

        boolean hasNull = requests.stream()
                .anyMatch(request -> !StringUtils.hasText(request.getMobile()) || Objects.isNull(request.getHouseHoldId()));
        if(hasNull){
            throw new IllegalArgumentException("list has null or empty value");
        }

        boolean hasMobileLengthNotEleven = requests.stream()
                .map(CreateMemberRequest::getMobile)
                .anyMatch(mobile -> mobile.length() != 11);
        if(hasMobileLengthNotEleven){
            throw new IllegalArgumentException("mobile length is not 11");
        }

        List<Long> householdIds = requests.stream()
                .map(CreateMemberRequest::getHouseHoldId)
                .collect(Collectors.toList());

        List<HouseHold> houseHolds = houseHoldRepository.findAllById(householdIds);

        if(houseHolds.size() != householdIds.size()){
            throw new NotFoundException("household is not found");
        }

        List<MemberInvite> inviteCodes = new ArrayList<>();
        for(int i = 0; i < requests.size(); i++){
            HouseHold houseHold = houseHolds.get(i);
            CreateMemberRequest request = requests.get(i);
            String mobile = request.getMobile();

            inviteCodes.add(MemberInvite.of(houseHold, Mobile.of(mobile), RandomStringUtils.randomNumeric(6)));
        }

        memberInviteCodeRepository.saveAll(inviteCodes);

        for(MemberInvite inviteCode : inviteCodes){
            alertCreateMemberService.alert(inviteCode.getMobile().toString(), inviteCode.getCode());
        }
    }
}
