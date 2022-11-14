package com.dutmdcjf.authserver.jwt;

import com.dutmdcjf.authserver.dto.mapper.UserMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.SysexMessage;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final UserMapper userMapper;

    @Value("${jwt.secretKey}")
    private String secretkey;
    private Key securityKey;

    @PostConstruct
    public void init() {
        this.securityKey = Keys.hmacShaKeyFor(secretkey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String id, String exp) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        String username = userMapper.getUserById(id).getUsername();

        String token = Jwts.builder()
                .setHeader(headers)
                .setIssuer("dutmdcjf")  //발급자
                .setSubject(id)         //토큰 제목 - 토큰 식별자
                .setAudience(username)  //토큰 대상자
                .setIssuedAt(new Date(System.currentTimeMillis()))                       //토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + Long.valueOf(exp))) //토큰 만료 시간
                .signWith(securityKey, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public String getHeaderToken(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null) {
            return request.getHeader("Authorization").substring(7);
        }
        return null;
    }
}
