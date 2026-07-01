package com.example.tripagent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tripagent.common.enums.ResponseCodeEnum;
import com.example.tripagent.common.exception.BusinessException;
import com.example.tripagent.domain.entity.BusinessTravelRequest;
import com.example.tripagent.domain.entity.ComplianceResult;
import com.example.tripagent.domain.entity.TravelPlan;
import com.example.tripagent.mapper.ComplianceResultMapper;
import com.example.tripagent.service.ComplianceResultService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComplianceResultServiceImpl
        extends ServiceImpl<ComplianceResultMapper, ComplianceResult>
        implements ComplianceResultService {

    @Override
    public void removeByRequestId(Long requestId) {
        if (requestId == null) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }

        remove(new QueryWrapper<ComplianceResult>().eq("request_id", requestId));
    }

    @Override
    public void savePlanResults(Long requestId, BusinessTravelRequest request, List<TravelPlan> plans) {
        if (requestId == null || request == null) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }

        if (plans == null || plans.isEmpty()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        List<ComplianceResult> results = new ArrayList<>();

        for (TravelPlan plan : plans) {
            if (plan == null || plan.getId() == null) {
                continue;
            }

            ComplianceResult result = new ComplianceResult();
            result.setRequestId(requestId);
            result.setPlanId(plan.getId());
            result.setCheckItem("PLAN_COMPLIANCE");
            result.setCheckStatus(plan.getComplianceStatus());
            result.setActualValue(plan.getTotalAmount() == null ? null : plan.getTotalAmount().toPlainString());
            result.setLimitValue(request.getBudgetAmount() == null ? null : request.getBudgetAmount().toPlainString());
            result.setMessage(limitLength(plan.getRiskSummary(), 500));
            result.setCreateTime(now);
            result.setUpdateTime(now);

            results.add(result);
        }

        if (!results.isEmpty()) {
            saveBatch(results);
        }
    }

    private String limitLength(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}