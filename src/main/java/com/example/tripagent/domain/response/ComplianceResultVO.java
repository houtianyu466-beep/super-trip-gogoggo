package com.example.tripagent.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplianceResultVO {

    private String checkItem;

    private String checkStatus;

    private String actualValue;

    private String limitValue;

    private String message;
}