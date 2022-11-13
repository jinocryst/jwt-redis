package com.dutmdcjf.authserver.dto;

import lombok.Data;

@Data
public class User {
    private Long idx;
    private String email;
    private String password;
    private String username;
}
