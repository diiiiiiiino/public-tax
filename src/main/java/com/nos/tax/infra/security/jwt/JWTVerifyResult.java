package com.nos.tax.infra.security.jwt;

import lombok.Builder;

@Builder
public class JWTVerifyResult {
    boolean isVerify;
    String loginId;
}
