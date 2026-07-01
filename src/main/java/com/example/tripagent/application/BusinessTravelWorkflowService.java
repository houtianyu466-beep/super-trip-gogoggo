package com.example.tripagent.application;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tripagent.agent.RequirementParseAgent;
import com.example.tripagent.common.enums.ResponseCodeEnum;
import com.example.tripagent.common.exception.BusinessException;
import com.example.tripagent.domain.constant.TravelRequestStatusConstant;
import com.example.tripagent.domain.dto.ParsedTravelRequirement;
import com.example.tripagent.domain.entity.BusinessTravelRequest;
import com.example.tripagent.domain.entity.Employee;
import com.example.tripagent.domain.entity.TravelPlan;
import com.example.tripagent.domain.entity.TravelPolicy;
import com.example.tripagent.domain.request.BusinessTravelPlanRequest;
import com.example.tripagent.domain.response.BusinessTravelPlanResponse;
import com.example.tripagent.domain.response.TravelPlanVO;
import com.example.tripagent.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BusinessTravelWorkflowService {

    private final RequirementParseAgent requirementParseAgent;
    private final EmployeeService employeeService;
    private final TravelPolicyService travelPolicyService;
    private final BusinessTravelRequestService businessTravelRequestService;
    private final CandidateOptionMockService candidateOptionMockService;
    private final TravelPlanService travelPlanService;
    private final AuditLogService auditLogService;

    public BusinessTravelWorkflowService(RequirementParseAgent requirementParseAgent,
                                         EmployeeService employeeService,
                                         TravelPolicyService travelPolicyService,
                                         BusinessTravelRequestService businessTravelRequestService,
                                         CandidateOptionMockService candidateOptionMockService,
                                         TravelPlanService travelPlanService,
                                         AuditLogService auditLogService) {
        this.requirementParseAgent = requirementParseAgent;
        this.employeeService = employeeService;
        this.travelPolicyService = travelPolicyService;
        this.businessTravelRequestService = businessTravelRequestService;
        this.candidateOptionMockService = candidateOptionMockService;
        this.travelPlanService = travelPlanService;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public BusinessTravelPlanResponse generatePlan(BusinessTravelPlanRequest request) {
        validateRequest(request);

        String requestNo = generateRequestNo();

        ParsedTravelRequirement requirement;
        try {
            requirement = requirementParseAgent.parse(request.getMessage());

            auditLogService.recordSuccess(
                    null,
                    "RequirementParseAgent",
                    "PARSE_REQUIREMENT",
                    request.getMessage(),
                    requirement.toString()
            );
        } catch (Exception e) {
            auditLogService.recordFailure(
                    null,
                    "RequirementParseAgent",
                    "PARSE_REQUIREMENT",
                    request.getMessage(),
                    e
            );
            throw e;
        }
        validateRequirement(requirement);

        Employee employee = employeeService.getActiveEmployeeById(request.getEmployeeId());
        TravelPolicy policy = travelPolicyService.getByTravelLevel(employee.getTravelLevel());

        BusinessTravelRequest travelRequest = buildTravelRequest(request, requestNo, requirement);
        businessTravelRequestService.save(travelRequest);

        candidateOptionMockService.prepareOptions(travelRequest);

        List<TravelPlan> plans = travelPlanService.generatePlans(travelRequest.getId());
        TravelPlan recommendedPlan = plans.isEmpty() ? null : plans.get(0);

        BusinessTravelPlanResponse response = new BusinessTravelPlanResponse();
        response.setRequestNo(requestNo);
        response.setRequirement(requirement);
        response.setPlans(plans.stream().map(this::convertToVO).collect(Collectors.toList()));
        response.setRecommendedPlan(recommendedPlan == null ? null : convertToVO(recommendedPlan));
        response.setApprovalText(recommendedPlan == null ? null : recommendedPlan.getApprovalText());
        response.setAuditId(null);

        return response;
    }

    private BusinessTravelRequest buildTravelRequest(BusinessTravelPlanRequest request,
                                                     String requestNo,
                                                     ParsedTravelRequirement requirement) {
        BusinessTravelRequest travelRequest = new BusinessTravelRequest();
        travelRequest.setRequestNo(requestNo);
        travelRequest.setEmployeeId(request.getEmployeeId());
        travelRequest.setDepartureCity(requirement.getDepartureCity());
        travelRequest.setDestinationCity(requirement.getDestinationCity());
        travelRequest.setTravelDays(requirement.getTravelDays());
        travelRequest.setReason(requirement.getReason());
        travelRequest.setProjectName(requirement.getProjectName());
        travelRequest.setBudgetAmount(requirement.getBudgetAmount());
        travelRequest.setNeedTransport(requirement.getNeedTransport());
        travelRequest.setNeedHotel(requirement.getNeedHotel());
        travelRequest.setRawMessage(request.getMessage());
        travelRequest.setStatus(TravelRequestStatusConstant.GENERATED);
        travelRequest.setCreateTime(LocalDateTime.now());
        travelRequest.setUpdateTime(LocalDateTime.now());

        if (requirement.getStartDate() != null) {
            travelRequest.setStartDate(requirement.getStartDate());
        }

        if (requirement.getEndDate() != null) {
            travelRequest.setEndDate(requirement.getEndDate());
        }

        return travelRequest;
    }

    private TravelPlanVO convertToVO(TravelPlan plan) {
        TravelPlanVO vo = new TravelPlanVO();

        vo.setPlanId(plan.getId());
        vo.setPlanName(plan.getPlanName());
        vo.setTransportAmount(plan.getTransportAmount());
        vo.setHotelAmount(plan.getHotelAmount());
        vo.setMealAmount(plan.getMealAmount());
        vo.setLocalTransportAmount(plan.getLocalTransportAmount());
        vo.setTotalAmount(plan.getTotalAmount());
        vo.setComplianceStatus(plan.getComplianceStatus());
        vo.setScore(plan.getScore());
        vo.setRiskSummary(plan.getRiskSummary());
        vo.setRecommendReason(plan.getRecommendReason());

        return vo;
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
        if (requirement.getTravelDays() == null || requirement.getTravelDays() <= 0) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }
        if (requirement.getBudgetAmount() == null || requirement.getBudgetAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }
        if (requirement.getReason() == null || requirement.getReason().trim().isEmpty()) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }
    }

    private void validateRequest(BusinessTravelPlanRequest request) {
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
        return "BT-" + System.currentTimeMillis() + "-"
                + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
    public BusinessTravelPlanResponse getPlanDetailByRequestNo(String requestNo) {
        if (requestNo == null || requestNo.trim().isEmpty()) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }

        BusinessTravelRequest travelRequest = businessTravelRequestService.getOne(
                new QueryWrapper<BusinessTravelRequest>()
                        .eq("request_no", requestNo)
        );

        if (travelRequest == null) {
            throw new BusinessException(ResponseCodeEnum.RESOURCE_NOT_FOUND);
        }

        List<TravelPlan> plans = travelPlanService.list(
                new QueryWrapper<TravelPlan>()
                        .eq("request_id", travelRequest.getId())
                        .orderByDesc("score")
                        .orderByAsc("total_amount")
        );

        TravelPlan recommendedPlan = plans.isEmpty() ? null : plans.get(0);

        BusinessTravelPlanResponse response = new BusinessTravelPlanResponse();
        response.setRequestNo(travelRequest.getRequestNo());
        response.setRequirement(convertToRequirement(travelRequest));
        response.setPlans(plans.stream().map(this::convertToVO).collect(Collectors.toList()));
        response.setRecommendedPlan(recommendedPlan == null ? null : convertToVO(recommendedPlan));
        response.setApprovalText(recommendedPlan == null ? null : recommendedPlan.getApprovalText());
        response.setAuditId(null);

        return response;
    }

    private ParsedTravelRequirement convertToRequirement(BusinessTravelRequest request) {
        ParsedTravelRequirement requirement = new ParsedTravelRequirement();

        requirement.setDepartureCity(request.getDepartureCity());
        requirement.setDestinationCity(request.getDestinationCity());
        requirement.setStartDate(request.getStartDate());
        requirement.setEndDate(request.getEndDate());
        requirement.setTravelDays(request.getTravelDays());
        requirement.setReason(request.getReason());
        requirement.setProjectName(request.getProjectName());
        requirement.setBudgetAmount(request.getBudgetAmount());
        requirement.setNeedTransport(request.getNeedTransport());
        requirement.setNeedHotel(request.getNeedHotel());

        return requirement;
    }
}
