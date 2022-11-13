package com.dutmdcjf.authserver.service;

import com.dutmdcjf.authserver.dto.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public Map<String, Object> userSignIn(String email, String password) {
        Map<String, Object> userSignInData;
        userSignInData = userMapper.getUserBySignIn(email, password);

        return userSignInData;
    }
}
