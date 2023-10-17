package com.nos.tax.household.command.application;

import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.validator.RequestValidator;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.HouseHolder;
import com.nos.tax.household.command.domain.enumeration.HouseHoldState;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 세대주를 변경하기 위한 서비스
 * 세대에 세대주가 없을 경우에 변경이 가능하다
 */
@Service
@RequiredArgsConstructor
public class HouseHolderChangeService {

    private final HouseHoldRepository houseHoldRepository;
    private final MemberRepository memberRepository;

    /**
     * 세대주 변경
     * @param houseHoldId 세대 ID
     * @param memberId 회원 ID
     * @throws HouseHoldNotFoundException 세대 미조회
     * @throws MemberNotFoundException 회원 미조회
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code houseHoldId}가 {@code null}인 경우
     *     <li>{@code memberId}가 {@code null}인 경우
     * </ul>
     */
    @Transactional
    public void change(Long houseHoldId, Long memberId) {
        List<ValidationError> errors = validateRequest(houseHoldId, memberId);
        if(!errors.isEmpty()){
            throw new ValidationErrorException("Request has invalid values", errors);
        }

        HouseHold houseHold = houseHoldRepository.findByIdAndHouseHoldState(houseHoldId, HouseHoldState.EMPTY)
                .orElseThrow(() -> new HouseHoldNotFoundException("HouseHold not found"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        houseHold.moveInHouse(HouseHolder.of(member));
    }

    /**
     * 데이터 유효성 체크
     * @param houseHoldId 세대 ID
     * @param memberId 회원 ID
     * @return {@code List<ValidationError>}
     */
    private List<ValidationError> validateRequest(Long houseHoldId, Long memberId){
        List<ValidationError> errors = new ArrayList<>();

        RequestValidator.validateId(houseHoldId, "houseHoldId", errors);
        RequestValidator.validateId(memberId, "memberId", errors);

        return errors;
    }
}
