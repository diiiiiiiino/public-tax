package com.nos.tax.household.command.application;

import com.nos.tax.application.exception.NotFoundException;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.enumeration.HouseHoldState;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HouseHoldMoveOutService {

    private final HouseHoldRepository houseHoldRepository;

    @Transactional
    public void leave(Long houseHoldId) {
        HouseHold houseHold = houseHoldRepository.findByIdAndHouseHoldState(houseHoldId, HouseHoldState.LIVE)
                .orElseThrow(() -> new NotFoundException("HouseHold not found"));

        houseHold.moveOutHouse();
    }
}
