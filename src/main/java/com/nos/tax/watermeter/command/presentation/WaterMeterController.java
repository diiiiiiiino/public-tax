package com.nos.tax.watermeter.command.presentation;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.http.Response;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.security.SecurityMember;
import com.nos.tax.watermeter.command.application.dto.WaterMeterCreateRequest;
import com.nos.tax.watermeter.command.application.exception.WaterMeterDeleteStateException;
import com.nos.tax.watermeter.command.application.exception.WaterMeterNotFoundException;
import com.nos.tax.watermeter.command.application.service.WaterMeterCreateService;
import com.nos.tax.watermeter.command.application.service.WaterMeterDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/water-meter")
@RequiredArgsConstructor
public class WaterMeterController {

    private final WaterMeterCreateService waterMeterCreateService;
    private final WaterMeterDeleteService waterMeterDeleteService;

    /**
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
    @PostMapping
    public Response<Void> create(
            @AuthenticationPrincipal SecurityMember securityMember, 
            @RequestBody WaterMeterCreateRequest request
    ){
        waterMeterCreateService.create(securityMember.getMemberId(), request);

        return Response.ok();
    }

    /**
     * @param id
     * @return Response
     * @throws WaterMeterNotFoundException 수도 계량 미조회
     * @throws WaterMeterDeleteStateException 수도 계량 삭제 조건 미충족
     */
    @DeleteMapping("/{id}")
    public Response<Void> delete(
            @PathVariable Long id
    ){
        waterMeterDeleteService.delete(id);
        return Response.ok();
    }
}
