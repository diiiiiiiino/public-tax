package com.nos.tax.member.command.infra;

import com.nos.tax.member.command.application.service.AlertCreateMemberService;
import com.nos.tax.util.VerifyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 회원 생성 요청 알림 서비스
 */
@Service
@RequiredArgsConstructor
public class AlertCreateMemberServiceImpl implements AlertCreateMemberService {

    /**
     * 회원 생성 요청 알림
     * @param mobile 전화번호
     * @param inviteCode 초대코드
     */
    public void alert(String mobile, String inviteCode) {
        VerifyUtil.verifyText(mobile, "memberMobile");
        VerifyUtil.verifyText(inviteCode, "memberInviteCode");

        /*todo : 카톡 알림 API 연동 필요*/

    }
}
