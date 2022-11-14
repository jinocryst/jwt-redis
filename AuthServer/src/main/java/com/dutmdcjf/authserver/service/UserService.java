package com.dutmdcjf.authserver.service;

import com.dutmdcjf.authserver.dto.mapper.UserMapper;
import com.dutmdcjf.authserver.jwt.AuthToken;
import com.dutmdcjf.authserver.jwt.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Value("${jwt.accessToken.exp}")
    private String accessTokenExp;

    @Value("${jwt.refreshToken.exp}")
    private String refreshTokenExp;

    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;


    /*
     * SingIn
     * */
    public AuthToken userSignIn(String email, String password) throws Exception {
        Map<String, Object> userSignInData;

        userSignInData = userMapper.getUserBySignIn(email, password);
        if (userSignInData == null) {
            throw new Exception();
        }

        String userId = String.valueOf(userSignInData.get("idx"));
        String accessToken = jwtProvider.createToken(userId, accessTokenExp);
        String refreshToken = jwtProvider.createToken(userId, refreshTokenExp);

        return new AuthToken(accessToken, refreshToken);
    }

    /*
     * refresh
     * */
    public AuthToken refresh(AuthToken authToken) throws Exception {
        String newAccessToken = null;

        try {
            if (!jwtProvider.isValidToken(authToken.getRefreshToken())) {
                log.info("refreshToken의 유효기간이 만료됨");
            }
            String userId = jwtProvider.getUserId(authToken.getRefreshToken());
            newAccessToken = jwtProvider.createToken(userId, accessTokenExp);
        } catch (ExpiredJwtException e) {
            log.warn(e.getMessage());
        }
        return new AuthToken(newAccessToken, null);
    }
}
