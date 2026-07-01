package com.example.tripagent.service;

import com.example.tripagent.domain.request.LoginRequest;
import com.example.tripagent.domain.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}