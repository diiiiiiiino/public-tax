package com.nos.tax.waterbill.query;

import com.nos.tax.waterbill.command.application.exception.WaterBillNotFoundException;
import com.nos.tax.waterbill.command.domain.WaterBill;
import com.nos.tax.watermeter.query.ThisMonthWaterMeterDto;
import com.nos.tax.watermeter.query.WaterMeterQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WaterBillQueryService {
    private final WaterBillQueryRepository waterBillQueryRepository;
    private final WaterMeterQueryService waterMeterQueryService;

    /**
     * 금월 수도요금 조회
     * @param buildingId 건물 ID
     * @param yearMonth 정산년월
     * @return ThisMonthWaterBillInfo
     * @throws WaterBillNotFoundException 수도요금 정보 미조회
     */
    @Transactional(readOnly = true)
    public ThisMonthWaterBillInfo getThisMonthWaterBillInfo(Long buildingId, YearMonth yearMonth){
        WaterBill waterBill = waterBillQueryRepository.findByBuildingIdAndCalculateYm(buildingId, yearMonth)
                .orElseThrow(() -> new WaterBillNotFoundException("WaterBill not found"));

        List<ThisMonthWaterMeterDto> thisMonthWaterMeters = waterMeterQueryService.getThisMonthWaterMeters(buildingId, yearMonth);
        
        return ThisMonthWaterBillInfo.of(waterBill, thisMonthWaterMeters);
    }
}
