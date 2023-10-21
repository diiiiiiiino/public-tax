package com.nos.tax.waterbill.command.presentation.controller;

import com.nos.tax.building.command.application.BuildingNotFoundException;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.http.Response;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.waterbill.command.application.dto.WaterBillCreateRequest;
import com.nos.tax.waterbill.command.application.exception.WaterBillNotFoundException;
import com.nos.tax.waterbill.command.application.service.WaterBillCalculateAppService;
import com.nos.tax.waterbill.command.application.service.WaterBillCreateService;
import com.nos.tax.waterbill.command.domain.exception.WaterBillDuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/admin/water-bill")
@RequiredArgsConstructor
public class AdminWaterBillController {

    private final WaterBillCreateService waterBillCreateService;
    private final WaterBillCalculateAppService waterBillCalculateAppService;

    /**
     * 수도요금 생성
     * @param admin 관리자
     * @param request 수도요금 생성 요청
     * @return Response
     * @throws BuildingNotFoundException 건물 미조회
     * @throws WaterBillDuplicateException 수도요금 중복
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code request}가 {@code null}일 때
     *     <li>{@code totalAmount}가 {@code null}이거나 음수일때
     *     <li>{@code calculateYm}가 {@code null}일 때
     * </ul>
     */
    @PostMapping
    public Response<Void> createWaterBill(Member admin, @RequestBody WaterBillCreateRequest request){
        waterBillCreateService.create(admin.getId(), request);

        return Response.ok();
    }

    /**
     * 수도요금 정산
     * @param admin 관리자
     * @param calculateYm 정산년월
     * @throws BuildingNotFoundException 건물 미조회
     * @throws WaterBillNotFoundException 수도요금 미조회
     */
    @PostMapping("/calculate/{calculateYm}")
    public Response<Void> calculateWaterBill(Member admin, @PathVariable YearMonth calculateYm){
        waterBillCalculateAppService.calculate(admin.getId(), calculateYm);

        return Response.ok();
    }
}
