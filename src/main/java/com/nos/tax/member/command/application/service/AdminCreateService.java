package com.nos.tax.member.command.application.service;

import com.nos.tax.building.command.domain.Address;
import com.nos.tax.building.command.domain.Building;
import com.nos.tax.building.command.domain.repository.BuildingRepository;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.member.command.application.dto.AdminCreateRequest;
import com.nos.tax.member.command.application.dto.BuildingInfo;
import com.nos.tax.member.command.application.dto.HouseHoldInfo;
import com.nos.tax.member.command.application.dto.MemberCreateRequest;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    @Transactional
    public void create(AdminCreateRequest request) {
        Objects.requireNonNull(request.getMemberCreateRequest());
        Objects.requireNonNull(request.getBuildingInfo());
        verifyHouseHoldInfos(request.getHouseHoldInfos());

        BuildingInfo buildingInfo = request.getBuildingInfo();
        List<HouseHoldInfo> houseHoldInfos = request.getHouseHoldInfos();

        Member admin = MemberCreateRequest.newAdmin(request.getMemberCreateRequest());
        Address address = Address.of(buildingInfo.getAddress1(), buildingInfo.getAddress2(), buildingInfo.getZipNo());

        List<Function<Building, HouseHold>> households = houseHoldInfos.stream()
                .map(houseHoldInfo ->
                        houseHoldInfo.isChecked() ?
                        (Function<Building, HouseHold>) (building -> HouseHold.of(houseHoldInfo.getRoom(), building, admin)) :
                        (Function<Building, HouseHold>) (building -> HouseHold.of(houseHoldInfo.getRoom(), building))
                ).collect(Collectors.toList());

        Building building = Building.of(buildingInfo.getName(), address, households);

        memberRepository.save(admin);
        buildingRepository.save(building);
    }

    private void verifyHouseHoldInfos(List<HouseHoldInfo> houseHoldInfos){
        final long SELECT_HOUSEHOLD_COUNT = 1;
        VerifyUtil.verifyCollection(houseHoldInfos);

        long checkCount = houseHoldInfos.stream()
                .filter(houseHoldInfo -> houseHoldInfo.isChecked())
                .count();

        if(checkCount != SELECT_HOUSEHOLD_COUNT){
            throw new IllegalArgumentException("select just one HouseHold");
        }
    }
}
