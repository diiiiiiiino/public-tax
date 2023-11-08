package com.nos.tax.watermeter.query;

import com.nos.tax.common.http.Paging;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaterMeterQueryService {
    private final ThisMonthWaterMeterRepository thisMonthWaterMeterRepository;
    private final TotalMonthWaterMeterRepository totalMonthWaterMeterRepository;

    /**
     * 금월 수도계량 조회
     *
     * @param pageable
     * @param search 조회 정보
     * @return
     */
    @Transactional(readOnly = true)
    public Paging<List<ThisMonthWaterMeter>> getTotalMonthWaterMeters(Pageable pageable, ThisMonthWaterMeterSearch search){
        return thisMonthWaterMeterRepository.getThisMonthWaterMeters(pageable, search);
    }

    /**
     * 월별 수도계량 조회
     *
     * @param search 조회 정보
     * @return
     */
    @Transactional(readOnly = true)
    public List<TotalMonthWaterMeter> getTotalMonthWaterMeters(TotalMonthWaterMeterSearch search){
        return totalMonthWaterMeterRepository.getTotalMonthWaterMeters(search);
    }
}
