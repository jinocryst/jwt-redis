package com.dutmdcjf.authserver.controller;

import com.dutmdcjf.authserver.dto.User;
import com.dutmdcjf.authserver.jwt.AuthToken;
import com.dutmdcjf.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signIn")
    public AuthToken signIn(@RequestBody User user) throws Exception {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            throw new Exception();
        }
        return userService.userSignIn(user.getEmail(), user.getPassword());
    }
}
