package com.example.tripagent.service;

import com.example.tripagent.domain.dto.BudgetPlanDTO;
import com.example.tripagent.domain.entity.BusinessTravelRequest;
import com.example.tripagent.domain.entity.TravelPolicy;

public interface BudgetPlanService  {
    BudgetPlanDTO calculateBudget(BusinessTravelRequest request, TravelPolicy policy);
}
