package com.nos.tax.member.command.application.service;

import com.nos.tax.application.exception.NotFoundException;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.invite.command.domain.MemberInviteCode;
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

        List<Long> householdIds = requests.stream()
                .map(CreateMemberRequest::getHouseHoldId)
                .collect(Collectors.toList());

        List<HouseHold> houseHolds = houseHoldRepository.findAllById(householdIds);

        if(houseHolds.size() != householdIds.size()){
            throw new NotFoundException("household is not found");
        }

        List<MemberInviteCode> inviteCodes = new ArrayList<>();
        for(int i = 0; i < requests.size(); i++){
            HouseHold houseHold = houseHolds.get(i);
            CreateMemberRequest request = requests.get(i);
            String mobile = request.getMobile();

            inviteCodes.add(MemberInviteCode.of(houseHold, Mobile.of(mobile.substring(0, 3), mobile.substring(3, 7), mobile.substring(7, 11)), RandomStringUtils.randomNumeric(6)));
        }

        memberInviteCodeRepository.saveAll(inviteCodes);

        for(MemberInviteCode inviteCode : inviteCodes){
            alertCreateMemberService.alert(inviteCode.getMobile().toString(), inviteCode.getCode());
        }
    }
}
