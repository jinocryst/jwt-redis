package com.dutmdcjf.authserver.controller;

import com.dutmdcjf.authserver.dto.User;
import com.dutmdcjf.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping ("/test")
    public Map<String, Object> test(@RequestBody User user) {
        return userService.userSignIn(user.getEmail(), user.getPassword());
    }
}
