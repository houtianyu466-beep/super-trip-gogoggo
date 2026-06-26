package com.example.tripagent.service.impl;

import com.example.tripagent.domain.dto.BudgetPlanDTO;
import com.example.tripagent.domain.dto.ComplianceCheckResultDTO;
import com.example.tripagent.domain.entity.HotelOption;
import com.example.tripagent.domain.entity.TransportOption;
import com.example.tripagent.domain.entity.TravelPlan;
import com.example.tripagent.service.PlanScoreService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PlanScoreServiceImpl implements PlanScoreService {

    @Override
    public Integer calculateScore(TravelPlan plan,
                                  HotelOption hotel,
                                  TransportOption transport,
                                  BudgetPlanDTO budgetPlan,
                                  ComplianceCheckResultDTO complianceResult) {
        int score = 100;

        if (complianceResult != null) {
            if ("REJECT".equals(complianceResult.getStatus())) {
                score -= 50;
            } else if ("WARNING".equals(complianceResult.getStatus())) {
                score -= 15;
            }
        }

        if (hotel != null && Boolean.TRUE.equals(hotel.getAgreementHotel())) {
            score += 10;
        }

        if (hotel != null && Boolean.TRUE.equals(hotel.getRecommended())) {
            score += 5;
        }

        if (transport != null && Boolean.TRUE.equals(transport.getRecommended())) {
            score += 5;
        }

        if (plan.getTotalAmount() != null && budgetPlan.getTotalBudget() != null
                && budgetPlan.getTotalBudget().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal usageRatio = plan.getTotalAmount()
                    .divide(budgetPlan.getTotalBudget(), 2, RoundingMode.HALF_UP);

            if (usageRatio.compareTo(new BigDecimal("0.70")) <= 0) {
                score += 15;
            } else if (usageRatio.compareTo(new BigDecimal("0.85")) <= 0) {
                score += 8;
            } else if (usageRatio.compareTo(BigDecimal.ONE) > 0) {
                score -= 20;
            }
        }

        return Math.max(score, 0);
    }
}