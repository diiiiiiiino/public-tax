package com.nos.tax.household.command.presentaion;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.http.Response;
import com.nos.tax.household.command.application.HouseHoldMoveOutService;
import com.nos.tax.household.command.application.HouseHolderChangeService;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 세대 관련 API를 처리하는 Controller
 */
@RestController
@RequestMapping("/household")
@RequiredArgsConstructor
public class HouseHoldController {

    private final HouseHoldMoveOutService houseHoldMoveOutService;
    private final HouseHolderChangeService houseHolderChangeService;

    /**
     * @param houseHoldId
     * @return Response
     * @throws HouseHoldNotFoundException 세대 미조회
     */
    @PostMapping(value = "/move-out/{houseHoldId}")
    public Response<Void> moveOut(@PathVariable Long houseHoldId){
        houseHoldMoveOutService.leave(houseHoldId);

        return Response.ok();
    }

    /**
     * @param houseHoldId
     * @return Response
     * @throws ValidationErrorException
     * @throws HouseHoldNotFoundException 세대 미조회
     * @throws MemberNotFoundException 회원 미조회
     */
    @PostMapping(value = "/householder/{houseHoldId}/{memberId}")
    public Response<Void> houseHolderChange(@PathVariable Long houseHoldId, @PathVariable Long memberId){
        houseHolderChangeService.change(houseHoldId, memberId);

        return Response.ok();
    }
}
