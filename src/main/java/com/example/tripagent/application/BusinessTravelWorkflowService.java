package com.example.tripagent.application;

import com.example.tripagent.common.enums.ResponseCodeEnum;
import com.example.tripagent.common.exception.BusinessException;
import com.example.tripagent.domain.dto.ParsedTravelRequirement;
import com.example.tripagent.domain.request.BusinessTravelPlanRequest;
import com.example.tripagent.domain.response.BusinessTravelPlanResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

@Service
public class BusinessTravelWorkflowService {
    public BusinessTravelPlanResponse generatePlan(BusinessTravelPlanRequest request) {

        vaildateRequest(request);
        String requestNo = generateRequestNo();
        // 临时假解析，后面替换成 RequirementParseAgent
        ParsedTravelRequirement requirement = mockParseRequirement(request.getMessage());

        BusinessTravelPlanResponse response = new BusinessTravelPlanResponse();
        response.setRequestNo(requestNo);
        response.setRequirement(requirement);
        response.setPlans(Collections.emptyList());
        response.setRecommendedPlan(null);
        response.setApprovalText("当前已完成出差需求解析，后续将生成差旅方案和审批说明。");
        response.setAuditId(null);

        return response;
    }

    private void vaildateRequest(BusinessTravelPlanRequest request){
        if (request == null) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }

        if (request.getEmployeeId() == null) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }

        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }
    }

    private String generateRequestNo() {
        //订单号生成
        String requestNo = "BT-"+System.currentTimeMillis()+"-"+ UUID.randomUUID().toString().replace("-", "").substring(0,8);
        return requestNo;
    }

    private ParsedTravelRequirement mockParseRequirement(String message) {
        ParsedTravelRequirement requirement = new ParsedTravelRequirement();
        requirement.setDepartureCity("杭州");
        requirement.setDestinationCity("北京");
        requirement.setTravelDays(2);
        requirement.setReason("客户会议");
        requirement.setProjectName("客户拜访项目");
        requirement.setBudgetAmount(new BigDecimal("3000"));
        requirement.setNeedTransport(true);
        requirement.setNeedHotel(true);
        return requirement;
    }
}
