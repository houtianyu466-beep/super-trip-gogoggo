package com.example.tripagent.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetPlanDTO {
    private BigDecimal totalBudget;
    private BigDecimal transportBudget;
    private BigDecimal hotelBudget;
    private BigDecimal mealBudget;
    private BigDecimal localTransportBudget;
    private BigDecimal reserveBudget;
    private BigDecimal hotelLimitPerNight;
    private BigDecimal mealAllowancePerDay;
    private BigDecimal localTransportPerDay;
    private Boolean approvalRequired;
}
