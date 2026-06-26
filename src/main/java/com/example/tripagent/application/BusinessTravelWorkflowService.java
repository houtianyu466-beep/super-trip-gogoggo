package com.example.tripagent.application;

import com.example.tripagent.agent.RequirementParseAgent;
import com.example.tripagent.common.enums.ResponseCodeEnum;
import com.example.tripagent.common.exception.BusinessException;
import com.example.tripagent.domain.dto.ParsedTravelRequirement;
import com.example.tripagent.domain.entity.Employee;
import com.example.tripagent.domain.entity.TravelPolicy;
import com.example.tripagent.domain.request.BusinessTravelPlanRequest;
import com.example.tripagent.domain.response.BusinessTravelPlanResponse;
import com.example.tripagent.service.EmployeeService;
import com.example.tripagent.service.TravelPolicyService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

@Service
public class BusinessTravelWorkflowService {

    private final RequirementParseAgent requirementParseAgent;
    private final EmployeeService employeeService;
    private final TravelPolicyService travelPolicyService;
    public BusinessTravelWorkflowService(RequirementParseAgent requirementParseAgent, EmployeeService employeeService, TravelPolicyService travelPolicyService) {
        this.requirementParseAgent = requirementParseAgent;
        this.employeeService = employeeService;
        this.travelPolicyService = travelPolicyService;
    }

    public BusinessTravelPlanResponse generatePlan(BusinessTravelPlanRequest request) {

        vaildateRequest(request);
        String requestNo = generateRequestNo();
        ParsedTravelRequirement requirement = requirementParseAgent.parse(request.getMessage());
        Employee employee = employeeService.getActiveEmployeeById(request.getEmployeeId());
        TravelPolicy policy = travelPolicyService.getByTravelLevel(employee.getTravelLevel());
        validateRequirement(requirement);

        BusinessTravelPlanResponse response = new BusinessTravelPlanResponse();
        response.setRequestNo(requestNo);
        response.setRequirement(requirement);
        response.setPlans(Collections.emptyList());
        response.setRecommendedPlan(null);
        response.setApprovalText(
                "已完成需求解析，并查询到员工：" + employee.getName()
                        + "，差旅等级：" + employee.getTravelLevel()
                        + "，酒店标准：" + policy.getHotelLimitPerNight() + "元/晚"
        );
        response.setAuditId(null);

        return response;
    }

    private void validateRequirement(ParsedTravelRequirement requirement) {
        if (requirement == null) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }
        if (requirement.getDepartureCity() == null || requirement.getDepartureCity().trim().isEmpty()) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }
        if (requirement.getDestinationCity() == null || requirement.getDestinationCity().trim().isEmpty()) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }
        if(requirement.getTravelDays()==null||requirement.getTravelDays()<=0){
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }
        if(requirement.getBudgetAmount()==null||requirement.getBudgetAmount().compareTo(BigDecimal.ZERO)<=0){
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }
        if(requirement.getReason()==null||requirement.getReason().trim().isEmpty()){
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }
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

}
