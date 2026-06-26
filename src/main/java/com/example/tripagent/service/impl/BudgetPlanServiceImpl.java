package com.example.tripagent.service.impl;

import com.example.tripagent.common.exception.BusinessException;
import com.example.tripagent.domain.dto.BudgetPlanDTO;
import com.example.tripagent.domain.entity.BusinessTravelRequest;
import com.example.tripagent.domain.entity.TravelPolicy;
import com.example.tripagent.service.BudgetPlanService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BudgetPlanServiceImpl implements BudgetPlanService {
    @Override
    public BudgetPlanDTO calculateBudget(BusinessTravelRequest request, TravelPolicy policy) {
        if (request == null || policy == null) {
            throw new BusinessException("参数错误");
        }
        if (request.getBudgetAmount() == null || request.getTravelDays() == null || request.getTravelDays() <= 0) {
            throw new BusinessException("预算或出差天数不能为空");
        }

        BigDecimal totalBudget = request.getBudgetAmount();

        int nights = request.getNeedHotel() != null && request.getNeedHotel()
                ? Math.max(request.getTravelDays() - 1, 0)
                : 0;

        BigDecimal hotelBudget = policy.getHotelLimitPerNight()
                .multiply(BigDecimal.valueOf(nights));

        BigDecimal mealBudget = policy.getMealAllowancePerDay()
                .multiply(BigDecimal.valueOf(request.getTravelDays()));

        BigDecimal localTransportBudget = policy.getLocalTransportPerDay()
                .multiply(BigDecimal.valueOf(request.getTravelDays()));

        BigDecimal reserveBudget = totalBudget.multiply(new BigDecimal("0.10"));

        BigDecimal transportBudget = totalBudget
                .subtract(hotelBudget)
                .subtract(mealBudget)
                .subtract(localTransportBudget)
                .subtract(reserveBudget);

        Boolean approvalRequired = totalBudget.compareTo(policy.getApprovalRequiredAmount()) >= 0;

        return new BudgetPlanDTO(
                totalBudget,
                transportBudget,
                hotelBudget,
                mealBudget,
                localTransportBudget,
                reserveBudget,
                policy.getHotelLimitPerNight(),
                policy.getMealAllowancePerDay(),
                policy.getLocalTransportPerDay(),
                approvalRequired
        );
    }
}
