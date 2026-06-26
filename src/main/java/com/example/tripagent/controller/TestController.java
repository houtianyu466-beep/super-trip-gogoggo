package com.example.tripagent.controller;

import com.example.tripagent.common.result.Result;
import com.example.tripagent.domain.entity.TravelPlan;
import com.example.tripagent.service.TravelPlanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    private final TravelPlanService travelPlanService;

    public TestController(TravelPlanService travelPlanService) {
        this.travelPlanService = travelPlanService;
    }

    @GetMapping("/plans")
    public Result<List<TravelPlan>> plans(@RequestParam Long requestId) {
        return Result.success(travelPlanService.generatePlans(requestId));
    }
}