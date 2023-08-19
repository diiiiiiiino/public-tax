package com.nos.tax.watermeter.command.application;

import com.nos.tax.common.exception.NotFoundException;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.watermeter.command.domain.repository.WaterMeter;
import com.nos.tax.watermeter.command.domain.repository.WaterMeterRepository;
import com.nos.tax.watermeter.query.WaterMeterCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WaterMeterCreateService {

    private final HouseHoldRepository houseHoldRepository;
    private final WaterMeterRepository waterMeterRepository;

    @Transactional
    public void create(WaterMeterCreateRequest request, Member member) {
        HouseHold houseHold = houseHoldRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new NotFoundException("Household not found"));

        waterMeterRepository.save(WaterMeter.of(request.getPreviousMeter(), request.getPresentMeter(), request.getYearMonth(), houseHold));
    }
}
