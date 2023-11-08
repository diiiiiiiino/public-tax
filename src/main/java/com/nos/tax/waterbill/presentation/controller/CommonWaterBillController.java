package com.nos.tax.waterbill.presentation.controller;

import com.nos.tax.common.http.response.PagingResponse;
import com.nos.tax.member.command.application.security.SecurityMember;
import com.nos.tax.waterbill.query.ThisMonthWaterBillInfo;
import com.nos.tax.waterbill.query.WaterBillQueryService;
import com.nos.tax.watermeter.query.ThisMonthWaterMeterSearch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RequestMapping("/common/water-bill")
@RestController
@RequiredArgsConstructor
public class CommonWaterBillController {
    private final WaterBillQueryService waterBillQueryService;

    @Operation(summary = "금월 수도요금 조회", description = "금월 수도요금 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상"),
            @ApiResponse(responseCode = "404", description = "건물 미조회"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping
    PagingResponse<ThisMonthWaterBillInfo> getThisMonthWaterMeters(
            @AuthenticationPrincipal SecurityMember securityMember,
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam YearMonth calculateYm,
            @RequestParam(required = false) Long houseHoldId){
        return PagingResponse.ok(waterBillQueryService.getThisMonthWaterBillInfo(pageable, ThisMonthWaterMeterSearch.of(securityMember.getBuildingId(), calculateYm, houseHoldId)));
    }
}
