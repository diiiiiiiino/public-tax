package com.nos.tax.household.query;

import com.nos.tax.household.command.domain.HouseHold;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseHoldQueryService {

    private final HouseHoldQueryRepository houseHoldQueryRepository;

    public List<HouseHold> getHouseHolds(Long buildingId){
        return houseHoldQueryRepository.findAllByBuilding(buildingId);
    }

}
