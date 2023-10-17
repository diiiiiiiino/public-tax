package com.nos.tax.household.command.application;

import com.nos.tax.common.exception.CustomNullPointerException;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.enumeration.HouseHoldState;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.util.VerifyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 세대 이사 서비스
 * 세대에 세대주가 존재해야 이사 처리가 가능하다
 */
@Service
@RequiredArgsConstructor
public class HouseHoldMoveOutService {

    private final HouseHoldRepository houseHoldRepository;

    /**
     * 세대 이사 처리
     * @param houseHoldId 세대 ID
     * @throws CustomNullPointerException 세대 ID가 {@code null}인 경우
     * @throws HouseHoldNotFoundException 세대 미조회
     */
    @Transactional
    public void leave(Long houseHoldId) {
        VerifyUtil.verifyNull(houseHoldId, "houseHoldId");

        HouseHold houseHold = houseHoldRepository.findByIdAndHouseHoldState(houseHoldId, HouseHoldState.LIVE)
                .orElseThrow(() -> new HouseHoldNotFoundException("HouseHold not found"));

        houseHold.moveOutHouse();
    }
}
