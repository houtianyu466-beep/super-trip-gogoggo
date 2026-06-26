package com.example.tripagent.service.impl;

import com.example.tripagent.domain.dto.ComplianceCheckResultDTO;
import com.example.tripagent.domain.entity.*;
import com.example.tripagent.service.ComplianceCheckService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComplianceCheckServiceImpl implements ComplianceCheckService {

    @Override
    public ComplianceCheckResultDTO check(TravelPlan plan,
                                          HotelOption hotel,
                                          TransportOption transport,
                                          BusinessTravelRequest request,
                                          TravelPolicy policy) {
        List<String> riskMessages = new ArrayList<>();

        boolean hasRejectRisk = false;
        boolean hasWarningRisk = false;

        if (plan == null || request == null || policy == null) {
            throw new IllegalArgumentException("合规校验参数不能为空");
        }

        // 1. 酒店标准校验
        if (hotel != null && hotel.getPricePerNight() != null && policy.getHotelLimitPerNight() != null) {
            if (hotel.getPricePerNight().compareTo(policy.getHotelLimitPerNight()) > 0) {
                hasRejectRisk = true;
                riskMessages.add("酒店单晚价格 " + hotel.getPricePerNight()
                        + " 元，超过差旅标准 " + policy.getHotelLimitPerNight() + " 元");
            }
        }

        // 2. 交通标准校验
        if (transport != null && policy.getTransportLevel() != null) {
            String transportKey = buildTransportKey(transport);

            if (!policy.getTransportLevel().contains(transportKey)) {
                hasRejectRisk = true;
                riskMessages.add("交通标准 " + transportKey
                        + " 不在员工差旅等级允许范围内：" + policy.getTransportLevel());
            }
        }

        // 3. 总预算校验
        if (plan.getTotalAmount() != null && request.getBudgetAmount() != null) {
            if (plan.getTotalAmount().compareTo(request.getBudgetAmount()) > 0) {
                hasRejectRisk = true;
                riskMessages.add("方案总费用 " + plan.getTotalAmount()
                        + " 元，超过申请预算 " + request.getBudgetAmount() + " 元");
            }
        }

        // 4. 审批门槛校验
        if (plan.getTotalAmount() != null && policy.getApprovalRequiredAmount() != null) {
            if (plan.getTotalAmount().compareTo(policy.getApprovalRequiredAmount()) >= 0) {
                hasWarningRisk = true;
                riskMessages.add("方案总费用达到审批门槛，需要主管审批");
            }
        }

        String status;
        if (hasRejectRisk) {
            status = "REJECT";
        } else if (hasWarningRisk) {
            status = "WARNING";
        } else {
            status = "PASS";
        }

        if (riskMessages.isEmpty()) {
            riskMessages.add("方案符合当前差旅政策");
        }

        ComplianceCheckResultDTO result = new ComplianceCheckResultDTO();
        result.setStatus(status);
        result.setRiskMessages(riskMessages);
        return result;
    }

    private String buildTransportKey(TransportOption transport) {
        return transport.getTransportType() + "_" + transport.getSeatLevel();
    }
}