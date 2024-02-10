package com.example.tasklist.service.impl;

import com.example.tasklist.service.AuthService;
import com.example.tasklist.web.dto.auth.JwtRequest;
import com.example.tasklist.web.dto.auth.JwtResponse;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public JwtResponse login(JwtRequest jwtRequest) {
        return null;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return null;
    }
}
