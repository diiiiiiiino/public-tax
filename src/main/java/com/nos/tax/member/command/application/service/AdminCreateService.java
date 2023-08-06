package com.nos.tax.member.command.application.service;

import com.nos.tax.building.command.domain.Address;
import com.nos.tax.building.command.domain.Building;
import com.nos.tax.building.command.domain.repository.BuildingRepository;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.member.command.application.dto.AdminCreateRequest;
import com.nos.tax.member.command.application.dto.BuildingInfo;
import com.nos.tax.member.command.application.dto.HouseHoldInfo;
import com.nos.tax.util.VerifyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCreateService {

    private final BuildingRepository buildingRepository;

    @Transactional
    public Long create(AdminCreateRequest request) {
        Objects.requireNonNull(request.getBuildingInfo());
        VerifyUtil.verifyList(request.getHouseHoldInfos());

        BuildingInfo buildingInfo = request.getBuildingInfo();
        List<HouseHoldInfo> houseHoldInfos = request.getHouseHoldInfos();

        Address address = Address.of(buildingInfo.getAddress1(), buildingInfo.getAddress2(), buildingInfo.getZipNo());

        List<Function<Building, HouseHold>> households = houseHoldInfos.stream()
                .map(houseHoldInfo -> (Function<Building, HouseHold>) (building -> HouseHold.of(houseHoldInfo.getRoom(), building)))
                .collect(Collectors.toList());

        Building building = Building.of(buildingInfo.getName(), address, households);

        building = buildingRepository.save(building);

        return building.getId();
    }
}
