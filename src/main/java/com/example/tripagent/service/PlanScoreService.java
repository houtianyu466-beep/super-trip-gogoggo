package com.example.tripagent.service;

import com.example.tripagent.domain.dto.BudgetPlanDTO;
import com.example.tripagent.domain.dto.ComplianceCheckResultDTO;
import com.example.tripagent.domain.entity.HotelOption;
import com.example.tripagent.domain.entity.TransportOption;
import com.example.tripagent.domain.entity.TravelPlan;

public interface PlanScoreService {

    Integer calculateScore(TravelPlan plan,
                           HotelOption hotel,
                           TransportOption transport,
                           BudgetPlanDTO budgetPlan,
                           ComplianceCheckResultDTO complianceResult);
}