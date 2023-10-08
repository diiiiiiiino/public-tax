package com.nos.tax.watermeter.command.application.service;

import com.nos.tax.waterbill.command.domain.repository.WaterBillRepository;
import com.nos.tax.watermeter.command.application.exception.WaterMeterDeleteStateException;
import com.nos.tax.watermeter.command.application.exception.WaterMeterNotFoundException;
import com.nos.tax.watermeter.command.domain.WaterMeter;
import com.nos.tax.watermeter.command.domain.WaterMeterState;
import com.nos.tax.watermeter.command.domain.repository.WaterMeterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WaterMeterDeleteService {

    private final WaterMeterRepository waterMeterRepository;
    private final WaterBillRepository waterBillRepository;

    /**
     * 수도 계량 삭제
     * @param id 수도 계량 ID
     * @throws WaterMeterNotFoundException 수도 계량 미조회
     * @throws WaterMeterDeleteStateException 수도 계량 삭제 조건 미충족
     */
    @Transactional
    public void delete(Long id) {
        WaterMeter waterMeter = waterMeterRepository.findByIdAndState(id, WaterMeterState.ACTIVATION)
                .orElseThrow(() -> new WaterMeterNotFoundException("WaterMeter not found"));

        waterBillRepository.findByWaterMeter(waterMeter.getId())
                .ifPresent(waterBill -> {
                    throw new WaterMeterDeleteStateException("WaterMeter can be deleted before the water bill is settled");
                });

        waterMeter.updateState(WaterMeterState.DEACTIVATION);
    }
}
