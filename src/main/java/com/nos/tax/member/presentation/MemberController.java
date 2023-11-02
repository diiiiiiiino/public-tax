package com.nos.tax.member.presentation;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.http.Response;
import com.nos.tax.member.command.application.dto.MemberCreateRequest;
import com.nos.tax.member.command.application.dto.MemberInfoChangeRequest;
import com.nos.tax.member.command.application.dto.PasswordChangeRequest;
import com.nos.tax.member.command.application.exception.ExpiredInviteCodeException;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.exception.InviteCodeNotFoundException;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.application.security.SecurityMember;
import com.nos.tax.member.command.application.service.MemberCreateService;
import com.nos.tax.member.command.application.service.MemberInfoChangeService;
import com.nos.tax.member.command.application.service.MemberWithdrawService;
import com.nos.tax.member.command.application.service.PasswordChangeService;
import com.nos.tax.member.command.domain.exception.PasswordNotMatchedException;
import com.nos.tax.member.command.domain.exception.PasswordOutOfConditionException;
import com.nos.tax.member.command.domain.exception.UpdatePasswordSameException;
import com.nos.tax.member.query.MemberDto;
import com.nos.tax.member.query.MemberQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberCreateService memberCreateService;
    private final MemberInfoChangeService memberInfoChangeService;
    private final PasswordChangeService passwordChangeService;
    private final MemberWithdrawService memberWithdrawService;
    private final MemberQueryService memberQueryService;

    /**
     * @param request 멤버 생성 요청
     * @return Response
     * @throws ValidationErrorException 유효하지 않은 값이 있을때
     * @throws InviteCodeNotFoundException 초대코드가 조회되지 않을 때
     * @throws ExpiredInviteCodeException 초대코드가 만료됐을 때
     * @throws HouseHoldNotFoundException 세대가 조회되지 않을 때
     * @throws PasswordOutOfConditionException 비밀번호 정책에 맞지 않을 때
     */
    @Operation(summary = "회원 생성", description = "회원 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 값이 있을때 / 초대코드 만료 / 비밀번호 정책에 맞지 않을 때"),
            @ApiResponse(responseCode = "404", description = "초대코드 / 세대 미조회"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping
    public Response<Void> createMember(@RequestBody MemberCreateRequest request){
        memberCreateService.create(request);
        return Response.ok();
    }

    /**
     * @param securityMember 인증완료 회원
     * @param request
     * @return Response
     * @throws ValidationErrorException 유효하지 않은 값이 있을때
     * @throws MemberNotFoundException 회원이 조회되지 않을 때
     */
    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 값이 있을때 / 초대코드 만료 / 비밀번호 정책에 맞지 않을 때"),
            @ApiResponse(responseCode = "404", description = "초대코드 / 세대 미조회"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PatchMapping
    public Response<Void> updateMember(@AuthenticationPrincipal SecurityMember securityMember, @RequestBody MemberInfoChangeRequest request){
        memberInfoChangeService.change(securityMember.getMember().getId(), request);
        return Response.ok();
    }

    /**
     * @param securityMember 인증완료 회원
     * @param request
     * @return Response
     * @throws ValidationErrorException 유효하지 않은 값이 있을때
     * @throws MemberNotFoundException 회원이 조회되지 않을때
     * @throws PasswordNotMatchedException 비밀번호가 틀렸을때
     * @throws UpdatePasswordSameException 변경하는 비밀번호가 기존가 동일할때
     */
    @Operation(summary = "회원 생성", description = "회원 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상"),
            @ApiResponse(responseCode = "400", description = "비밀번호가 틀렸을때 / 변경하는 비밀번호가 기존가 동일할때"),
            @ApiResponse(responseCode = "404", description = "회원 미조회"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PatchMapping("/password")
    public Response<Void> updatePassword(@AuthenticationPrincipal SecurityMember securityMember, @RequestBody PasswordChangeRequest request){
        passwordChangeService.change(securityMember.getMember().getId(), request);
        return Response.ok();
    }

    /**
     * @param securityMember 인증완료 회원
     * @return {@code Http Response} 회원
     */
    @Operation(summary = "회원 조회", description = "회원 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping
    public Response<MemberDto> getMember(@AuthenticationPrincipal SecurityMember securityMember){
        return Response.ok(memberQueryService.getMember(securityMember.getMemberId()));
    }

    /**
     * @param securityMember 인증완료 회원
     * @return Response
     * @throws MemberNotFoundException 회원이 조회되지 않을때
     * @throws HouseHoldNotFoundException 세대가 조회되지 않을때
     */
    @Operation(summary = "회원 생성", description = "회원 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상"),
            @ApiResponse(responseCode = "404", description = "회원 / 세대 미조회"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/withdraw")
    public Response<Void> withdraw(@AuthenticationPrincipal SecurityMember securityMember){
        memberWithdrawService.withDraw(securityMember.getMember());
        return Response.ok();
    }
}
