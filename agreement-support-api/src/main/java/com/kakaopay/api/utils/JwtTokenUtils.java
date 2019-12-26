package com.kakaopay.api.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtTokenUtils {
    private static final String ISSUER = "kakaopay";
    private static final int TOKEN_EXPIRED_MINUTES = 30;

    public static String createToken(String secretKey, String memberId) {
        try {

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withClaim("memberId", memberId)
                    .withIssuedAt(new Date())
                    .withExpiresAt(getExpiredAt())
                    .sign(signing(secretKey));
        } catch (JWTCreationException | NullPointerException exception) {
            log.error("exception={}", exception.getMessage());
        }

        return null;
    }

    public static boolean verifyToken(String secretKey, String token) {
        try {

            JWTVerifier verifier = JWT.require(signing(secretKey))
                    .withIssuer(ISSUER)
                    .acceptLeeway(1)
                    .acceptExpiresAt(5)
                    .build();

            DecodedJWT jwt = verifier.verify(token);
            log.info("issuedAt={}, expiresAt={}", jwt.getIssuedAt(), jwt.getExpiresAt());
            return true;
        } catch (JWTVerificationException exception) {
            log.error("exception={}", exception.getMessage());
        }
        return false;
    }

    public static DecodedToken decodeToken(String token) {
        DecodedJWT decode = JWT.decode(token);

        return DecodedToken.builder()
                .issuer(decode.getIssuer())
                .memberId(decode.getClaim("memberId").asString())
                .build();
    }

    private static Algorithm signing(String secretKey) {
        if (StringUtils.isEmpty(secretKey)) {
            throw new NullPointerException("secretKey is Null");
        }

        return Algorithm.HMAC256(secretKey);
    }

    /**
     * ttl : 30 minutes
     */
    private static Date getExpiredAt() {

        return Date.from(LocalDateTime.now().plusMinutes(TOKEN_EXPIRED_MINUTES).atZone(ZoneId.systemDefault()).toInstant());
    }
}
