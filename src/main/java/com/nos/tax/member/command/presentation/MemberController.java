package com.nos.tax.member.command.presentation;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.http.Response;
import com.nos.tax.member.command.application.dto.MemberCreateRequest;
import com.nos.tax.member.command.application.dto.MemberInfoChangeRequest;
import com.nos.tax.member.command.application.dto.PasswordChangeRequest;
import com.nos.tax.member.command.application.exception.ExpiredInviteCodeException;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.exception.InviteCodeNotFoundException;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.application.service.MemberCreateService;
import com.nos.tax.member.command.application.service.MemberInfoChangeService;
import com.nos.tax.member.command.application.service.PasswordChangeService;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.exception.PasswordNotMatchedException;
import com.nos.tax.member.command.domain.exception.PasswordOutOfConditionException;
import com.nos.tax.member.command.domain.exception.UpdatePasswordSameException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberCreateService memberCreateService;
    private final MemberInfoChangeService memberInfoChangeService;
    private final PasswordChangeService passwordChangeService;

    /**
     * @param request
     * @return Response
     * @throws ValidationErrorException 유효하지 않은 값이 있을때
     * @throws InviteCodeNotFoundException 초대코드가 조회되지 않을 때
     * @throws ExpiredInviteCodeException 초대코드가 만료됐을 때
     * @throws HouseHoldNotFoundException 세대가 조회되지 않을 때
     * @throws PasswordOutOfConditionException 비밀번호 정책에 맞지 않을 때
     */
    @PostMapping
    public Response<Void> createMember(@RequestBody MemberCreateRequest request){
        memberCreateService.create(request);
        return Response.ok();
    }

    /**
     * @param member
     * @param request
     * @return Response
     * @throws ValidationErrorException 유효하지 않은 값이 있을때
     * @throws MemberNotFoundException 회원이 조회되지 않을 때
     */
    @PatchMapping
    public Response<Void> updateMember(Member member, @RequestBody MemberInfoChangeRequest request){
        memberInfoChangeService.change(member, request);
        return Response.ok();
    }

    /**
     * @param member
     * @param request
     * @return Response
     * @throws ValidationErrorException 유효하지 않은 값이 있을때
     * @throws MemberNotFoundException 회원이 조회되지 않을때
     * @throws PasswordNotMatchedException 비밀번호가 틀렸을때
     * @throws UpdatePasswordSameException 변경하는 비밀번호가 기존가 동일할때
     */
    @PatchMapping("/password")
    public Response<Void> updatePassword(Member member, @RequestBody PasswordChangeRequest request){
        passwordChangeService.change(member, request);
        return Response.ok();
    }
}
