package com.nos.tax.waterbill.application;

import com.nos.tax.building.domain.Building;
import com.nos.tax.building.domain.repository.BuildingRepository;
import com.nos.tax.member.domain.Member;
import com.nos.tax.util.VerifyUtil;
import com.nos.tax.waterbill.domain.WaterBill;
import com.nos.tax.waterbill.domain.repository.WaterBillRepository;
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
        VerifyUtil.verifyNegative(totalAmount);
        Objects.requireNonNull(calculateYm);

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
