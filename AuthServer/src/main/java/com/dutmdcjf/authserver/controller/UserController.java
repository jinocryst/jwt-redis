package com.dutmdcjf.authserver.controller;

import com.dutmdcjf.authserver.dto.User;
import com.dutmdcjf.authserver.jwt.AuthToken;
import com.dutmdcjf.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/signIn", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthToken signIn(@RequestBody User user) throws Exception {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            throw new Exception();
        }
        return userService.userSignIn(user.getEmail(), user.getPassword());
    }

    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthToken refresh(@RequestBody AuthToken authToken) throws Exception {
        if (authToken == null) {
            throw new Exception();
        }
        return userService.refresh(authToken);
    }
}
