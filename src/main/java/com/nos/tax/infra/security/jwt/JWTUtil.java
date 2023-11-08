package com.nos.tax.infra.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nos.tax.member.command.application.security.SecurityMember;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JWTUtil {
    private static final long AUTH_TIME = 7*24*60*60;
    private static Algorithm ALGORITHM;

    @Value("${JWT_SECRET}")
    public void setAlgorithm(String secretKey) {
        ALGORITHM = Algorithm.HMAC256(secretKey);
    }

    public String makeAuthToken(SecurityMember member) {
        return JWT.create()
                .withSubject(member.getUsername())
                .withClaim("exp", Instant.now().getEpochSecond() + AUTH_TIME)
                .sign(ALGORITHM);
    }

    public JWTVerifyResult verify(String token) {
        try{
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);

            return JWTVerifyResult.builder().isVerify(true)
                    .loginId(verify.getSubject())
                    .build();
        } catch (Exception ex){
            DecodedJWT decode = JWT.decode(token);
            return JWTVerifyResult.builder().isVerify(false)
                    .loginId(decode.getSubject())
                    .build();
        }
    }
}
