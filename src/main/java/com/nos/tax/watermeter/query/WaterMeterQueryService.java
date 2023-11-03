package com.nos.tax.watermeter.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WaterMeterQueryService {
    private final WaterMeterQueryDslRepository waterMeterQueryDslRepository;

    /**
     * 금월 수도계량 조회
     * @param buildingId 건물 ID
     * @param calculateYm 정산년월
     * @return
     */
    @Transactional(readOnly = true)
    public List<ThisMonthWaterMeterDto> getThisMonthWaterMeters(Long buildingId, YearMonth calculateYm){
        return waterMeterQueryDslRepository.getThisMonthWaterMeters(buildingId, calculateYm);
    }
}
