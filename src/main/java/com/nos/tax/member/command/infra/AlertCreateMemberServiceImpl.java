package com.nos.tax.member.command.infra;

import com.nos.tax.member.command.application.service.AlertCreateMemberService;
import com.nos.tax.util.VerifyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertCreateMemberServiceImpl implements AlertCreateMemberService {
    public void alert(String mobile, String inviteCode) {
        VerifyUtil.verifyText(mobile, "memberMobile");
        VerifyUtil.verifyText(inviteCode, "memberInviteCode");

        /*todo : 카톡 알림 API 연동 필요*/
    }
}
