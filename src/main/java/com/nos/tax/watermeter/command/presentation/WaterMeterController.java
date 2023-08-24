package com.nos.tax.watermeter.command.presentation;

import com.nos.tax.common.http.Response;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.watermeter.command.application.dto.WaterMeterCreateRequest;
import com.nos.tax.watermeter.command.application.service.WaterMeterCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/water-meter")
@RequiredArgsConstructor
public class WaterMeterController {

    private final WaterMeterCreateService waterMeterCreateService;

    @PostMapping
    public Response<Void> create(Member member, @RequestBody WaterMeterCreateRequest request){
        waterMeterCreateService.create(member.getId(), request);

        return Response.ok();
    }
}
