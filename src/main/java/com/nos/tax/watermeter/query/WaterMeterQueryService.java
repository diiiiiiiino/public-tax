package com.nos.tax.watermeter.query;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaterMeterQueryService {
    private final WaterMeterQueryDslRepository waterMeterQueryDslRepository;

    /**
     * 금월 수도계량 조회
     *
     * @param pageable
     * @param search 조회 정보
     * @return
     */
    @Transactional(readOnly = true)
    public List<ThisMonthWaterMeter> getThisMonthWaterMeters(Pageable pageable, ThisMonthWaterMeterSearch search){
        return waterMeterQueryDslRepository.getThisMonthWaterMeters(pageable, search);
    }
}
