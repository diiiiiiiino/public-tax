package com.nos.tax.member.presentation;

import com.nos.tax.authority.command.application.exception.AuthorityNotFoundException;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.http.Response;
import com.nos.tax.member.command.application.dto.AdminCreateRequest;
import com.nos.tax.member.command.application.dto.RequestCreateMemberRequest;
import com.nos.tax.member.command.application.exception.HouseHoldNotFoundException;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.application.security.SecurityMember;
import com.nos.tax.member.command.application.service.AdminChangeService;
import com.nos.tax.member.command.application.service.AdminCreateService;
import com.nos.tax.member.command.application.service.RequestCreateMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminCreateService adminCreateService;
    private final AdminChangeService adminChangeService;
    private final RequestCreateMemberService requestCreateMemberService;

    /**
     * 관리자 생성
     * @param request 관리자 생성 요청
     * @return Response<Void>
     * @throws ValidationErrorException 관리자 생성 요청 유효성 에러
     */
    @Operation(summary = "관리자 생성", description = "관리자 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상"),
            @ApiResponse(responseCode = "400", description = "관리자 생성 요청 유효성 에러"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping
    public Response<Void> createAdmin(@RequestBody AdminCreateRequest request){
        adminCreateService.create(request);

        return Response.ok();
    }

    /**
     * 관리자 변경
     * @param securityAdmin 인증 관리자
     * @param memberId 관리자로 변경할 회원 ID
     * @return Response<Void>
     * @throws MemberNotFoundException 관리자로 변경할 회원 미조회
     * @throws AuthorityNotFoundException 권한 미조회
     */
    @Operation(summary = "관리자 변경", description = "관리자 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상"),
            @ApiResponse(responseCode = "404", description = "관리자로 변경할 회원 / 권한 미조회 "),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PatchMapping("/{memberId}")
    public Response<Void> changeAdmin(
            @AuthenticationPrincipal SecurityMember securityAdmin,
            @PathVariable Long memberId
    ){
        adminChangeService.change(securityAdmin.getMember(), memberId);
        return Response.ok();
    }

    /**
     * 회원 생성 요청
     * @param requests 회원 생성 요청 대상 리스트
     * @return Response<Void>
     * @throws HouseHoldNotFoundException 세대 미조회
     * @throws ValidationErrorException 회원 생성 요청 대상 유효성 검증 에러
     */
    @Operation(summary = "관리자 변경", description = "관리자 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상"),
            @ApiResponse(responseCode = "400", description = "회원 생성 요청 대상 유효성 검증 에러"),
            @ApiResponse(responseCode = "404", description = "세대 미조회"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/member")
    public Response<Void> requestCreateMember(@RequestBody List<RequestCreateMemberRequest> requests){
        requestCreateMemberService.request(requests);

        return Response.ok();
    }

}
