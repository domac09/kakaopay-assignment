package com.kakaopay.api.service.auth;

import com.kakaopay.api.utils.DecodedToken;
import com.kakaopay.api.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Value("${secret-key}")
    private String secretKey;

    public String createToken(String memberId){
        return JwtTokenUtils.createToken(secretKey, memberId);
    }

    public boolean verifyToken(String token){
        return JwtTokenUtils.verifyToken(secretKey, token);
    }

    public DecodedToken decodedToken(String token){
        return JwtTokenUtils.decodeToken(token);
    }

    public String issueToken(String token) {
        DecodedToken decodedToken = decodedToken(token);

        return createToken(decodedToken.getMemberId());
    }
}
