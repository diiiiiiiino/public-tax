package com.nos.tax.waterbill.query;

import com.nos.tax.waterbill.command.application.exception.WaterBillNotFoundException;
import com.nos.tax.waterbill.command.domain.WaterBill;
import com.nos.tax.watermeter.query.ThisMonthWaterMeter;
import com.nos.tax.watermeter.query.ThisMonthWaterMeterSearch;
import com.nos.tax.watermeter.query.WaterMeterQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaterBillQueryService {
    private final WaterBillQueryRepository waterBillQueryRepository;
    private final WaterMeterQueryService waterMeterQueryService;

    /**
     * 금월 수도요금 조회
     * @param pageable 페이징 정보
     * @param search 조회 정보
     * @return ThisMonthWaterBillInfo
     * @throws WaterBillNotFoundException 수도요금 정보 미조회
     */
    @Transactional(readOnly = true)
    public ThisMonthWaterBillInfo getThisMonthWaterBillInfo(Pageable pageable, ThisMonthWaterMeterSearch search){
        WaterBill waterBill = waterBillQueryRepository.findByBuildingIdAndCalculateYm(search.getBuildingId(), search.getCalculateYm())
                .orElseThrow(() -> new WaterBillNotFoundException("WaterBill not found"));

        List<ThisMonthWaterMeter> thisMonthWaterMeters = waterMeterQueryService.getThisMonthWaterMeters(pageable, search);
        
        return ThisMonthWaterBillInfo.of(waterBill, thisMonthWaterMeters);
    }
}
