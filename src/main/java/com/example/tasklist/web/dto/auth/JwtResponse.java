package com.example.tasklist.web.dto.auth;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

    private Long id;
    private String username;
    private String accessToken;
    private String refreshToken;

}
