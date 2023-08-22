package com.nos.tax.waterbill.command.application.service;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.building.command.domain.repository.BuildingRepository;
import com.nos.tax.common.exception.NotFoundException;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.waterbill.command.domain.WaterBill;
import com.nos.tax.waterbill.command.domain.repository.WaterBillRepository;
import com.nos.tax.waterbill.command.domain.service.WaterBillCalculateService;
import com.nos.tax.watermeter.command.domain.repository.WaterMeter;
import com.nos.tax.watermeter.command.domain.repository.WaterMeterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WaterBillCalculateAppService {

    private final BuildingRepository buildingRepository;
    private final WaterBillRepository waterBillRepository;
    private final WaterMeterRepository waterMeterRepository;
    private final WaterBillCalculateService waterBillCalculateService;

    @Transactional
    public void calculate(Member member, YearMonth yearMonth) {
        Building building = buildingRepository.findByMember(member.getId())
                .orElseThrow(() -> new NotFoundException("Building not found"));

        WaterBill waterBill = waterBillRepository.findByBuildingAndCalculateYm(building, yearMonth)
                .orElseThrow(() -> new NotFoundException("WaterBill not found"));

        List<WaterMeter> waterMeters = waterMeterRepository.findAllByHouseHoldIn(building.getHouseHolds());

        waterBillCalculateService.calculate(building, waterBill, waterMeters);
    }
}
