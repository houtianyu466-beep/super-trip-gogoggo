package com.example.tripagent.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;

    private Long userId;

    private Long employeeId;

    private String username;

    private String role;
}