package com.dutmdcjf.authserver.service;

import com.dutmdcjf.authserver.common.StringDefiner;
import com.dutmdcjf.authserver.dto.mapper.UserMapper;
import com.dutmdcjf.authserver.jwt.AuthToken;
import com.dutmdcjf.authserver.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Value("${spring.redis.exp}")
    private Long redisExp;

    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;


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

        String redisId = StringDefiner.REDIS_ID_PREFIX + userId;
        String redisAuthToken = objectMapper.writeValueAsString(new AuthToken(accessToken, refreshToken));
        redisService.setValues(redisId, redisAuthToken, redisExp);

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
            String redisId = StringDefiner.REDIS_ID_PREFIX + userId;
            String redisAuthToken = String.valueOf(redisService.getValues(redisId));

            if (redisAuthToken == null) {
                throw new Exception();
            }

            String redisRefreshToken = objectMapper.readValue(redisAuthToken, AuthToken.class).getRefreshToken();

            if (!redisRefreshToken.equals(authToken.getRefreshToken())) {
                log.info("refreshToken이 다르므로, 재발급하지 않고 거절 응답");
                throw new Exception();
            }

            newAccessToken = jwtProvider.createToken(userId, accessTokenExp);
            String newRedisAuthToken = objectMapper.writeValueAsString(new AuthToken(newAccessToken, authToken.getRefreshToken()));
            redisService.setValues(redisId, newRedisAuthToken, redisExp);

        } catch (ExpiredJwtException e) {
            log.warn(e.getMessage());
        }

        return new AuthToken(newAccessToken, null);
    }
}
