package com.example.tripagent.controller;

import com.example.tripagent.application.BusinessTravelWorkflowService;
import com.example.tripagent.common.result.Result;
import com.example.tripagent.domain.request.BusinessTravelPlanRequest;
import com.example.tripagent.domain.response.BusinessTravelPlanResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/businessTravel")
public class BusinessTravelController {

    private final BusinessTravelWorkflowService businessTravelWorkflowService;
    public BusinessTravelController(BusinessTravelWorkflowService businessTravelWorkflowService) {
        this.businessTravelWorkflowService = businessTravelWorkflowService;
    }

    @PostMapping("/plan")
    public Result<BusinessTravelPlanResponse> plan(@Valid @RequestBody BusinessTravelPlanRequest request) {
        BusinessTravelPlanResponse businessTravelPlanResponse = businessTravelWorkflowService.generatePlan(request);
        return Result.success(businessTravelPlanResponse);
    }
}
