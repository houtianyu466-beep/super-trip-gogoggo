package com.example.tripagent.service.impl;

import com.example.tripagent.common.enums.ResponseCodeEnum;
import com.example.tripagent.common.exception.BusinessException;
import com.example.tripagent.domain.entity.UserAccount;
import com.example.tripagent.domain.request.LoginRequest;
import com.example.tripagent.domain.response.LoginResponse;
import com.example.tripagent.service.AuthService;
import com.example.tripagent.service.UserAccountService;
import com.example.tripagent.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    
    private final UserAccountService userAccountService;
    public AuthServiceImpl(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }
    @Override
    public LoginResponse login(LoginRequest request) {
        if (request == null
                || request.getUsername() == null || request.getUsername().trim().isEmpty()
                || request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }

        UserAccount user = userAccountService.getByUsername(request.getUsername());
        if(user == null){
            throw new BusinessException(ResponseCodeEnum.USER_NOT_EXIST);
        }
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new BusinessException(ResponseCodeEnum.USER_BE_DISABLED);
        }

        if (!request.getPassword().equals(user.getPasswordHash())) {
            throw new BusinessException(ResponseCodeEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        String token= JwtUtil.generateToken(user.getId(),
                user.getEmployeeId(),
                user.getUsername(),
                user.getRole());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setUserId(user.getId());
        loginResponse.setEmployeeId(user.getEmployeeId());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setRole(user.getRole());

        return loginResponse;
    }
}
