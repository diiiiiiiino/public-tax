package com.nos.tax.waterbill.command.application.controller;

import com.nos.tax.common.http.Response;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.waterbill.command.application.dto.WaterBillCreateRequest;
import com.nos.tax.waterbill.command.application.service.WaterBillCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/water-bill")
@RequiredArgsConstructor
public class WaterBillController {

    private final WaterBillCreateService waterBillCreateService;

    /**
     * @param admin
     * @param request
     * @return Response
     * @throws
     */
    @PostMapping
    public Response<Void> createWaterBill(Member admin, @RequestBody WaterBillCreateRequest request){
        waterBillCreateService.create(admin, request);

        return Response.ok();
    }
}
