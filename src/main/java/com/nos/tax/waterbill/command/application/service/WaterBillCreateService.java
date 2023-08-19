package com.nos.tax.waterbill.command.application.service;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.building.command.domain.repository.BuildingRepository;
import com.nos.tax.common.exception.NotFoundException;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.waterbill.command.domain.WaterBill;
import com.nos.tax.waterbill.command.domain.exception.WaterBillDuplicateException;
import com.nos.tax.waterbill.command.domain.repository.WaterBillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WaterBillCreateService {

    private final BuildingRepository buildingRepository;
    private final WaterBillRepository waterBillRepository;

    /**
     * 정산 담당자가 수도요금 정산 데이터 생성
     *
     * @param admin
     * @param totalAmount
     * @param calculateYm
     * @return WaterBill
     * @throws
     */
    @Transactional
    public WaterBill create(Member admin, int totalAmount, YearMonth calculateYm) {
        Objects.requireNonNull(admin);

        Building building = buildingRepository.findByMember(admin.getId())
                .orElseThrow(() -> new NotFoundException("Building is not found"));

        waterBillRepository.findByBuildingAndCalculateYm(building, calculateYm)
                .ifPresent((waterBill) -> {
                    throw new WaterBillDuplicateException(waterBill.getCalculateYm().toString() + " WaterBill is exists");
                });

        WaterBill waterBill = WaterBill.of(building, totalAmount, calculateYm);
        return waterBillRepository.save(waterBill);
    }
}
