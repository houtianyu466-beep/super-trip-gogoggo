package com.example.tripagent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tripagent.agent.ApprovalTextAgent;
import com.example.tripagent.common.enums.ResponseCodeEnum;
import com.example.tripagent.common.exception.BusinessException;
import com.example.tripagent.domain.dto.BudgetPlanDTO;
import com.example.tripagent.domain.dto.ComplianceCheckResultDTO;
import com.example.tripagent.domain.entity.*;
import com.example.tripagent.mapper.BusinessTravelRequestMapper;
import com.example.tripagent.mapper.EmployeeMapper;
import com.example.tripagent.mapper.TravelPlanMapper;
import com.example.tripagent.mapper.TravelPolicyMapper;
import com.example.tripagent.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TravelPlanServiceImpl
        extends ServiceImpl<TravelPlanMapper, TravelPlan>
        implements TravelPlanService{
    private final BusinessTravelRequestMapper businessTravelRequestMapper;
    private final EmployeeMapper employeeMapper;
    private final TravelPolicyMapper travelPolicyMapper;
    private final BudgetPlanService budgetPlanService;
    private final HotelOptionService hotelOptionService;
    private final TransportOptionService transportOptionService;
    private final ComplianceCheckService complianceCheckService;
    private final PlanScoreService planScoreService;
    private final ApprovalTextAgent approvalTextAgent;
    private final ComplianceResultService complianceResultService;
    private final AuditLogService auditLogService;

    public TravelPlanServiceImpl(BusinessTravelRequestMapper businessTravelRequestMapper,
                                 EmployeeMapper employeeMapper,
                                 TravelPolicyMapper travelPolicyMapper,
                                 BudgetPlanService budgetPlanService,
                                 HotelOptionService hotelOptionService,
                                 TransportOptionService transportOptionService,
                                 ComplianceCheckService complianceCheckService,
                                 PlanScoreService planScoreService,
                                 ApprovalTextAgent approvalTextAgent,
                                 ComplianceResultService complianceResultService,
                                 AuditLogService auditLogService) {
        this.businessTravelRequestMapper = businessTravelRequestMapper;
        this.employeeMapper = employeeMapper;
        this.travelPolicyMapper = travelPolicyMapper;
        this.budgetPlanService = budgetPlanService;
        this.hotelOptionService = hotelOptionService;
        this.transportOptionService = transportOptionService;
        this.complianceCheckService = complianceCheckService;
        this.planScoreService = planScoreService;
        this.approvalTextAgent = approvalTextAgent;
        this.complianceResultService = complianceResultService;
        this.auditLogService = auditLogService;
    }
    @Transactional
    @Override
    public List<TravelPlan> generatePlans(Long requestId) {
        if (requestId == null) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }

        BusinessTravelRequest request = businessTravelRequestMapper.selectById(requestId);
        if (request == null) {
            throw new BusinessException(ResponseCodeEnum.RESOURCE_NOT_FOUND);
        }

        Employee employee = employeeMapper.selectById(request.getEmployeeId());
        if (employee == null) {
            throw new BusinessException(ResponseCodeEnum.EMPLOYEE_NOT_EXIST);
        }

        String travelLevel = employee.getTravelLevel();
        TravelPolicy travelPolicy = travelPolicyMapper.selectOne(
                new QueryWrapper<TravelPolicy>().eq("travel_level", travelLevel)
        );
        if (travelPolicy == null) {
            throw new BusinessException("找不到对应的差旅政策");
        }

        BudgetPlanDTO budgetPlan = budgetPlanService.calculateBudget(request, travelPolicy);

        List<HotelOption> hotelOptions = Boolean.TRUE.equals(request.getNeedHotel())
                ? hotelOptionService.getVaildOptions(requestId)
                : Collections.singletonList(null);

        List<TransportOption> transportOptions = Boolean.TRUE.equals(request.getNeedTransport())
                ? transportOptionService.getVaildOptions(requestId)
                : Collections.singletonList(null);

        if (hotelOptions.isEmpty() || transportOptions.isEmpty()) {
            complianceResultService.removeByRequestId(requestId);
            this.remove(new QueryWrapper<TravelPlan>().eq("request_id", requestId));
            return Collections.emptyList();
        }

        List<TravelPlan> plans = new ArrayList<>();
        for (HotelOption hotel : hotelOptions) {
            for (TransportOption transport : transportOptions) {
                TravelPlan plan = buildPlan(request, hotel, transport, budgetPlan, travelPolicy);
                plans.add(plan);
            }
        }

        plans.sort((a, b) -> {
            int scoreCompare = b.getScore().compareTo(a.getScore());
            if (scoreCompare != 0) {
                return scoreCompare;
            }
            return a.getTotalAmount().compareTo(b.getTotalAmount());
        });

        if (!plans.isEmpty()) {
            TravelPlan recommendedPlan = plans.get(0);

            String approvalInput = buildApprovalInput(employee, request, recommendedPlan);

            try {
                String approvalText = approvalTextAgent.generateApprovalText(
                        defaultString(employee.getName()),
                        defaultString(employee.getDepartment()),
                        defaultString(employee.getTravelLevel()),
                        defaultString(request.getDepartureCity()),
                        defaultString(request.getDestinationCity()),
                        request.getTravelDays(),
                        defaultString(request.getReason()),
                        defaultString(request.getProjectName()),
                        defaultAmount(request.getBudgetAmount()),
                        defaultString(recommendedPlan.getPlanName()),
                        defaultAmount(recommendedPlan.getTransportAmount()),
                        defaultAmount(recommendedPlan.getHotelAmount()),
                        defaultAmount(recommendedPlan.getMealAmount()),
                        defaultAmount(recommendedPlan.getLocalTransportAmount()),
                        defaultAmount(recommendedPlan.getTotalAmount()),
                        defaultString(recommendedPlan.getComplianceStatus()),
                        defaultString(recommendedPlan.getRiskSummary()),
                        defaultString(recommendedPlan.getRecommendReason())
                );

                recommendedPlan.setApprovalText(approvalText);

                auditLogService.recordSuccess(
                        request.getId(),
                        "ApprovalTextAgent",
                        "GENERATE_APPROVAL_TEXT",
                        approvalInput,
                        approvalText
                );
            } catch (Exception e) {
                auditLogService.recordFailure(
                        request.getId(),
                        "ApprovalTextAgent",
                        "GENERATE_APPROVAL_TEXT",
                        approvalInput,
                        e
                );
                throw e;
            }
        }

        complianceResultService.removeByRequestId(requestId);
        this.remove(new QueryWrapper<TravelPlan>().eq("request_id", requestId));

        this.saveBatch(plans);

        List<TravelPlan> savedPlans = this.list(
                new QueryWrapper<TravelPlan>()
                        .eq("request_id", requestId)
                        .orderByDesc("score")
                        .orderByAsc("total_amount")
        );

        complianceResultService.savePlanResults(requestId, request, savedPlans);

        return savedPlans;
    }

    private String buildApprovalInput(Employee employee,
                                      BusinessTravelRequest request,
                                      TravelPlan recommendedPlan) {
        return "员工：" + defaultString(employee.getName())
                + "，部门：" + defaultString(employee.getDepartment())
                + "，差旅等级：" + defaultString(employee.getTravelLevel())
                + "，出发城市：" + defaultString(request.getDepartureCity())
                + "，目的城市：" + defaultString(request.getDestinationCity())
                + "，出差天数：" + request.getTravelDays()
                + "，出差事由：" + defaultString(request.getReason())
                + "，项目名称：" + defaultString(request.getProjectName())
                + "，申请预算：" + defaultAmount(request.getBudgetAmount())
                + "，推荐方案：" + defaultString(recommendedPlan.getPlanName())
                + "，总费用：" + defaultAmount(recommendedPlan.getTotalAmount())
                + "，合规状态：" + defaultString(recommendedPlan.getComplianceStatus())
                + "，风险说明：" + defaultString(recommendedPlan.getRiskSummary())
                + "，推荐理由：" + defaultString(recommendedPlan.getRecommendReason());
    }

    private String buildPlanName(HotelOption hotel, TransportOption transport) {
        String transportName = transport == null
                ? "无交通"
                : transport.getTransportType() + "-" + transport.getSeatLevel();

        String hotelName = hotel == null
                ? "无酒店"
                : hotel.getHotelName();

        return transportName + " + " + hotelName;
    }

    private String buildRecommendReason(HotelOption hotel,
                                        TransportOption transport,
                                        BigDecimal totalAmount,
                                        BudgetPlanDTO budgetPlan) {
        StringBuilder reason = new StringBuilder();

        reason.append("总费用").append(totalAmount).append("元");

        if (totalAmount.compareTo(budgetPlan.getTotalBudget()) <= 0) {
            reason.append("，未超过申请预算");
        } else {
            reason.append("，超过申请预算");
        }

        if (hotel != null && Boolean.TRUE.equals(hotel.getAgreementHotel())) {
            reason.append("，酒店为协议酒店");
        }

        if (transport != null && Boolean.TRUE.equals(transport.getRecommended())) {
            reason.append("，交通方案为推荐班次");
        }

        return reason.toString();
    }



    private TravelPlan buildPlan(BusinessTravelRequest request,
                                 HotelOption hotel,
                                 TransportOption transport,
                                 BudgetPlanDTO budgetPlan,
                                 TravelPolicy travelPolicy) {
        BigDecimal transportAmount = transport == null
                ? BigDecimal.ZERO
                : safeAmount(transport.getPrice());

        BigDecimal hotelAmount = hotel == null
                ? BigDecimal.ZERO
                : safeAmount(hotel.getTotalPrice());

        BigDecimal mealAmount = safeAmount(budgetPlan.getMealBudget());
        BigDecimal localTransportAmount = safeAmount(budgetPlan.getLocalTransportBudget());

        BigDecimal totalAmount = transportAmount
                .add(hotelAmount)
                .add(mealAmount)
                .add(localTransportAmount);

        TravelPlan plan = new TravelPlan();
        plan.setRequestId(request.getId());
        plan.setPlanName(buildPlanName(hotel, transport));
        plan.setTransportOptionId(transport == null ? null : transport.getId());
        plan.setHotelOptionId(hotel == null ? null : hotel.getId());

        plan.setTransportAmount(transportAmount);
        plan.setHotelAmount(hotelAmount);
        plan.setMealAmount(mealAmount);
        plan.setLocalTransportAmount(localTransportAmount);
        plan.setTotalAmount(totalAmount);

        ComplianceCheckResultDTO check = complianceCheckService.check(plan, hotel, transport, request, travelPolicy);
        plan.setComplianceStatus(check.getStatus());
        plan.setRiskSummary(String.join("；", check.getRiskMessages()));

        Integer score = planScoreService.calculateScore(
                plan,
                hotel,
                transport,
                budgetPlan,
                check
        );
        plan.setScore(score);
        plan.setRecommendReason(buildRecommendReason(hotel, transport, totalAmount, budgetPlan));

        plan.setApprovalText(null);
        plan.setCreateTime(LocalDateTime.now());
        plan.setUpdateTime(LocalDateTime.now());

        return plan;
    }
    private BigDecimal safeAmount(BigDecimal amount) {
        return amount == null ? BigDecimal.ZERO : amount;
    }





    private String defaultString(String value) {
        return value == null || value.trim().isEmpty() ? "未提供" : value;
    }

    private String defaultAmount(BigDecimal value) {
        return value == null ? "0" : value.toPlainString();
    }

}
