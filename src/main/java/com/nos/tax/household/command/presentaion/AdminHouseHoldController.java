package com.nos.tax.household.command.presentaion;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.http.Response;
import com.nos.tax.household.command.application.HouseHoldMoveOutService;
import com.nos.tax.household.command.application.HouseHolderChangeService;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 세대 관련 API를 처리하는 Controller
 */
@RestController
@RequestMapping("/admin/household")
@RequiredArgsConstructor
public class AdminHouseHoldController {

    private final HouseHoldMoveOutService houseHoldMoveOutService;
    private final HouseHolderChangeService houseHolderChangeService;

    /**
     * @param houseHoldId
     * @return Response
     * @throws HouseHoldNotFoundException 세대 미조회
     */
    @Operation(summary = "세대주 이사", description = "세대주 이사")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상"),
            @ApiResponse(responseCode = "404", description = "세대 미조회"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping(value = "/move-out/{houseHoldId}")
    public Response<Void> moveOut(@PathVariable Long houseHoldId){
        houseHoldMoveOutService.leave(houseHoldId);

        return Response.ok();
    }

    /**
     * @param houseHoldId
     * @return Response
     * @throws ValidationErrorException 요청 유효성 에러
     * @throws HouseHoldNotFoundException 세대 미조회
     * @throws MemberNotFoundException 회원 미조회
     */
    @Operation(summary = "세대주 변경", description = "세대주 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상"),
            @ApiResponse(responseCode = "400", description = "요청 유효성 에러"),
            @ApiResponse(responseCode = "404", description = "회원 / 세대 미조회"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping(value = "/householder/{houseHoldId}/{memberId}")
    public Response<Void> houseHolderChange(@PathVariable Long houseHoldId, @PathVariable Long memberId){
        houseHolderChangeService.change(houseHoldId, memberId);

        return Response.ok();
    }
}
