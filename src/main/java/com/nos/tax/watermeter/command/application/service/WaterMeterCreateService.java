package com.nos.tax.watermeter.command.application.service;

import com.nos.tax.common.exception.NotFoundException;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.validator.RequestValidator;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.watermeter.command.application.dto.WaterMeterCreateRequest;
import com.nos.tax.watermeter.command.application.validator.WaterMeterCreateRequestQualifier;
import com.nos.tax.watermeter.command.domain.repository.WaterMeter;
import com.nos.tax.watermeter.command.domain.repository.WaterMeterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WaterMeterCreateService {

    private final HouseHoldRepository houseHoldRepository;
    private final WaterMeterRepository waterMeterRepository;
    private final RequestValidator<WaterMeterCreateRequest> validator;

    public WaterMeterCreateService(HouseHoldRepository houseHoldRepository, WaterMeterRepository waterMeterRepository, @WaterMeterCreateRequestQualifier RequestValidator validator) {
        this.houseHoldRepository = houseHoldRepository;
        this.waterMeterRepository = waterMeterRepository;
        this.validator = validator;
    }

    @Transactional
    public void create(Long memberId, WaterMeterCreateRequest request) {
        List<ValidationError> errors = validator.validate(request);
        RequestValidator.validateId(memberId, "memberId", errors);

        if(!errors.isEmpty()){
            throw new ValidationErrorException("Request has invalid values", errors);
        }

        HouseHold houseHold = houseHoldRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException("Household not found"));

        waterMeterRepository.save(WaterMeter.of(request.getPreviousMeter(), request.getPresentMeter(), request.getCalculateYm(), houseHold));
    }
}
