package com.example.tripagent.common.auth;

import lombok.Data;

@Data
public class LoginUser {
    private Long userId;
    private Long employeeId;
    private String username;
    private String role;
}