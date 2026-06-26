package com.example.tripagent.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplianceCheckResultDTO {
    private String status; // PASS / WARNING / REJECT
    private List<String> riskMessages;
}