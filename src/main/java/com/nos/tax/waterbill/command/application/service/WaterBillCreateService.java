package com.nos.tax.waterbill.command.application.service;

import com.nos.tax.building.command.application.BuildingNotFoundException;
import com.nos.tax.building.command.domain.Building;
import com.nos.tax.building.command.domain.BuildingState;
import com.nos.tax.building.command.domain.repository.BuildingRepository;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.validator.RequestValidator;
import com.nos.tax.waterbill.command.application.dto.WaterBillCreateRequest;
import com.nos.tax.waterbill.command.application.validator.annotation.WaterBillCreateRequestQualifier;
import com.nos.tax.waterbill.command.domain.WaterBill;
import com.nos.tax.waterbill.command.domain.exception.WaterBillDuplicateException;
import com.nos.tax.waterbill.command.domain.repository.WaterBillRepository;
import com.nos.tax.watermeter.command.application.service.WaterMeterCreateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nos.tax.common.validator.RequestValidator.validateId;

/**
 * 수도요금 생성 서비스
 */
@Service
public class WaterBillCreateService {

    private final BuildingRepository buildingRepository;
    private final WaterBillRepository waterBillRepository;
    private final WaterMeterCreateService waterMeterCreateService;
    private final RequestValidator validator;

    public WaterBillCreateService(BuildingRepository buildingRepository,
                                  WaterBillRepository waterBillRepository,
                                  WaterMeterCreateService waterMeterCreateService,
                                  @WaterBillCreateRequestQualifier RequestValidator validator) {
        this.buildingRepository = buildingRepository;
        this.waterBillRepository = waterBillRepository;
        this.waterMeterCreateService = waterMeterCreateService;
        this.validator = validator;
    }

    /**
     * 정산 담당자가 수도요금 정산 데이터 생성
     *
     * @param memberId
     * @param request
     * @return WaterBill
     * @throws BuildingNotFoundException 건물 미조회
     * @throws WaterBillDuplicateException 수도요금 중복
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code request}가 {@code null}일 때
     *     <li>{@code totalAmount}가 {@code null}이거나 음수일때
     *     <li>{@code calculateYm}가 {@code null}일 때
     * </ul>
     */
    @Transactional
    public WaterBill create(Long memberId, WaterBillCreateRequest request) {
        List<ValidationError> errors = validator.validate(request);
        validateId(memberId, "memberId", errors);

        if(!errors.isEmpty()){
            throw new ValidationErrorException("Request has invalid values", errors);
        }

        Building building = buildingRepository.findByMember(memberId, BuildingState.ACTIVATION)
                .orElseThrow(() -> new BuildingNotFoundException("Building not found"));

        waterBillRepository.findByBuildingAndCalculateYm(building, request.getCalculateYm())
                .ifPresent((waterBill) -> {
                    throw new WaterBillDuplicateException(waterBill.getCalculateYm().toString() + " WaterBill is exists");
                });

        WaterBill waterBill = WaterBill.of(building, request.getTotalAmount(), request.getCalculateYm());
        return waterBillRepository.save(waterBill);
    }
}
