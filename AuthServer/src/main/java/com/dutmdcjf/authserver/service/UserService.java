package com.dutmdcjf.authserver.service;

import com.dutmdcjf.authserver.dto.mapper.UserMapper;
import com.dutmdcjf.authserver.jwt.AuthToken;
import com.dutmdcjf.authserver.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    @Value("${jwt.accessToken.exp}")
    private String accessTokenExp;

    @Value("${jwt.refreshToken.exp}")
    private String refreshTokenExp;

    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;

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
}
