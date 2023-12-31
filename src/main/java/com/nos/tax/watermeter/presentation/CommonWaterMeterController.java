package com.nos.tax.watermeter.presentation;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.http.response.DataResponse;
import com.nos.tax.common.http.response.Response;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.security.SecurityMember;
import com.nos.tax.watermeter.command.application.dto.WaterMeterCreateRequest;
import com.nos.tax.watermeter.command.application.exception.WaterMeterDeleteStateException;
import com.nos.tax.watermeter.command.application.exception.WaterMeterNotFoundException;
import com.nos.tax.watermeter.command.application.service.WaterMeterCreateService;
import com.nos.tax.watermeter.command.application.service.WaterMeterDeleteService;
import com.nos.tax.watermeter.query.TotalMonthWaterMeter;
import com.nos.tax.watermeter.query.TotalMonthWaterMeterSearch;
import com.nos.tax.watermeter.query.WaterMeterQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/common/water-meter")
@RequiredArgsConstructor
public class CommonWaterMeterController {

    private final WaterMeterCreateService waterMeterCreateService;
    private final WaterMeterDeleteService waterMeterDeleteService;
    private final WaterMeterQueryService waterMeterQueryService;

    /**
     * 수도계량 데이터 생성
     * @param securityMember 인증 회원
     * @param request 수도계량 생성 요청
     * @return Response
     * @throws ValidationErrorException 유효성 에러
     * <ul>
     *     <li>{@code previousMeter}가 음수일때</li>
     *     <li>{@code presentMeter}가 음수일때</li>
     *     <li>{@code calculateYm}이 {@code null}일때</li>
     * </ul>
     * @throws HouseHoldNotFoundException 세대 미조회
     */
    @Operation(summary = "수도계량 데이터 생성", description = "수도계량 데이터 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상"),
            @ApiResponse(responseCode = "400", description = "유효성 에러"),
            @ApiResponse(responseCode = "404", description = "세대 미조회"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping
    public Response create(
            @AuthenticationPrincipal SecurityMember securityMember, 
            @RequestBody WaterMeterCreateRequest request
    ){
        waterMeterCreateService.create(securityMember.getMemberId(), request);

        return Response.ok();
    }

    /**
     * 수도계량 데이터 삭제
     * @param id
     * @return Response
     * @throws WaterMeterNotFoundException 수도 계량 미조회
     * @throws WaterMeterDeleteStateException 수도 계량 삭제 조건 미충족
     */
    @Operation(summary = "수도계량 데이터 삭제", description = "수도계량 데이터 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상"),
            @ApiResponse(responseCode = "403", description = "수도 계량 삭제 조건 미충족"),
            @ApiResponse(responseCode = "404", description = "수도 계량 미조회"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @DeleteMapping("/{id}")
    public Response delete(
            @PathVariable Long id
    ){
        waterMeterDeleteService.delete(id);
        return Response.ok();
    }

    @Operation(summary = "월별 수도계량 조회", description = "월별 수도계량 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping
    public DataResponse<List<TotalMonthWaterMeter>> getTotalMonthWaterMeters(
            @AuthenticationPrincipal SecurityMember securityMember,
            @RequestParam YearMonth start,
            @RequestParam YearMonth end
    ){
        return DataResponse.ok(waterMeterQueryService.getTotalMonthWaterMeters(TotalMonthWaterMeterSearch.of(securityMember.getBuildingId(), securityMember.getHouseHoldId(), start, end)));
    }
}
