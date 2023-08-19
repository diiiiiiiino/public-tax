package com.nos.tax.member.command.presentation;

import com.nos.tax.common.http.Response;
import com.nos.tax.member.command.application.dto.MemberCreateRequest;
import com.nos.tax.member.command.application.dto.MemberUpdateRequest;
import com.nos.tax.member.command.application.dto.PasswordChangeRequest;
import com.nos.tax.member.command.application.service.MemberCreateService;
import com.nos.tax.member.command.application.service.MemberInfoChangeService;
import com.nos.tax.member.command.application.service.PasswordChangeService;
import com.nos.tax.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberCreateService memberCreateService;
    private final MemberInfoChangeService memberInfoChangeService;
    private final PasswordChangeService passwordChangeService;

    @PostMapping
    public Response<Void> createMember(@RequestBody MemberCreateRequest request){

        memberCreateService.create(request);

        return Response.ok();
    }

    @PatchMapping
    public Response<Void> updateMember(Member member, @RequestBody MemberUpdateRequest request){

        memberInfoChangeService.change(member, request);

        return Response.ok();
    }

    @PatchMapping("/password")
    public Response<Void> updatePassword(Member member, @RequestBody PasswordChangeRequest request){

        passwordChangeService.change(member, request);

        return Response.ok();
    }
}
