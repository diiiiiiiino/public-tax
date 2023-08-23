package com.nos.tax.waterbill.command.presentation.controller;

import com.nos.tax.common.http.Response;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.waterbill.command.application.dto.WaterBillCreateRequest;
import com.nos.tax.waterbill.command.application.service.WaterBillCalculateAppService;
import com.nos.tax.waterbill.command.application.service.WaterBillCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/water-bill")
@RequiredArgsConstructor
public class WaterBillController {

    private final WaterBillCreateService waterBillCreateService;
    private final WaterBillCalculateAppService waterBillCalculateAppService;

    /**
     * @param admin
     * @param request
     * @return Response
     * @throws
     */
    @PostMapping
    public Response<Void> createWaterBill(Member admin, @RequestBody WaterBillCreateRequest request){
        waterBillCreateService.create(admin.getId(), request);

        return Response.ok();
    }

    /**
     * @param admin
     * @param calculateYm
     * @return Response
     */
    @PostMapping("/calculate/{calculateYm}")
    public Response<Void> calculateWaterBill(Member admin, @PathVariable YearMonth calculateYm){
        waterBillCalculateAppService.calculate(admin.getId(), calculateYm);

        return Response.ok();
    }
}
