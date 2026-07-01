package com.example.tripagent.controller;

import com.example.tripagent.common.result.Result;
import com.example.tripagent.domain.request.LoginRequest;
import com.example.tripagent.domain.response.LoginResponse;
import com.example.tripagent.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }
}