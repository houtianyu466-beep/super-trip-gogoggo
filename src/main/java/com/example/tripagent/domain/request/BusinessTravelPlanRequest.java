package com.example.tripagent.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessTravelPlanRequest {
    @NotNull(message = "员工ID不能为空")
    private Long employeeId;
    @NotBlank(message = "原始消息不能为空")
    private String message;
}
