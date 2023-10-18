package com.nos.tax.waterbill.command.application.service;

import com.nos.tax.building.command.application.BuildingNotFoundException;
import com.nos.tax.building.command.domain.Building;
import com.nos.tax.building.command.domain.BuildingState;
import com.nos.tax.building.command.domain.repository.BuildingRepository;
import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.waterbill.command.application.exception.WaterBillNotFoundException;
import com.nos.tax.waterbill.command.domain.WaterBill;
import com.nos.tax.waterbill.command.domain.repository.WaterBillRepository;
import com.nos.tax.waterbill.command.domain.service.WaterBillCalculateService;
import com.nos.tax.watermeter.command.domain.WaterMeter;
import com.nos.tax.watermeter.command.domain.repository.WaterMeterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static com.nos.tax.common.validator.RequestValidator.validateId;

/**
 * 수도요금 계산 서비스
 */
@Service
@RequiredArgsConstructor
public class WaterBillCalculateAppService {

    private final BuildingRepository buildingRepository;
    private final WaterBillRepository waterBillRepository;
    private final WaterMeterRepository waterMeterRepository;
    private final WaterBillCalculateService waterBillCalculateService;

    /**
     * 수도요금 계산
     * @param memberId 회원 ID
     * @param calculateYm 수도요금 계산 년월
     * @throws BuildingNotFoundException 건물 미조회
     * @throws WaterBillNotFoundException 수도요금 미조회
     */
    @Transactional
    public void calculate(Long memberId, YearMonth calculateYm) {
        validateRequest(memberId, calculateYm);

        Building building = buildingRepository.findByMember(memberId, BuildingState.ACTIVATION)
                .orElseThrow(() -> new BuildingNotFoundException("Building not found"));

        WaterBill waterBill = waterBillRepository.findByBuildingAndCalculateYm(building, calculateYm)
                .orElseThrow(() -> new WaterBillNotFoundException("WaterBill not found"));

        List<WaterMeter> waterMeters = waterMeterRepository.findAllByHouseHoldInAndCalculateYm(building.getHouseHolds(), calculateYm);

        waterBillCalculateService.calculate(building, waterBill, waterMeters);
    }

    /**
     * 수도요금 계산 파라미터 유효성 검증
     * @param memberId 회원 ID
     * @param yearMonth 수도요금 계산 년월
     * @return List<ValidationError>
     */
    private List<ValidationError> validateRequest(Long memberId, YearMonth yearMonth){
        List<ValidationError> errors = new ArrayList<>();

        validateId(memberId, "memberId", errors);
        if(yearMonth == null){
            errors.add(ValidationError.of("calculateYm", ValidationCode.NULL.getValue()));
        }

        return errors;
    }
}
