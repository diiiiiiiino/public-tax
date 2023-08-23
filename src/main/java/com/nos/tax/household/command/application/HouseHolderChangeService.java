package com.nos.tax.household.command.application;

import com.nos.tax.common.exception.NotFoundException;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.validator.RequestValidator;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.HouseHolder;
import com.nos.tax.household.command.domain.enumeration.HouseHoldState;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseHolderChangeService {

    private final HouseHoldRepository houseHoldRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void change(Long houseHoldId, Long memberId) {
        List<ValidationError> errors = validateRequest(houseHoldId, memberId);
        if(!errors.isEmpty()){
            throw new ValidationErrorException("Request has invalid values", errors);
        }

        HouseHold houseHold = houseHoldRepository.findByIdAndHouseHoldState(houseHoldId, HouseHoldState.EMPTY)
                .orElseThrow(() -> new HouseHoldNotFoundException("HouseHold not found"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found"));

        houseHold.moveInHouse(HouseHolder.of(member, member.getName(), member.getMobile()));
    }

    private List<ValidationError> validateRequest(Long houseHoldId, Long memberId){
        List<ValidationError> errors = new ArrayList<>();

        RequestValidator.validateId(houseHoldId, "houseHoldId", errors);
        RequestValidator.validateId(memberId, "memberId", errors);

        return errors;
    }
}
