package com.example.tripagent.controller;

import com.example.tripagent.common.result.Result;
import com.example.tripagent.domain.entity.TravelPlan;
import com.example.tripagent.mapper.TravelPlanMapper;
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
    private final TravelPlanMapper travelPlanMapper;

    public TestController(TravelPlanService travelPlanService,
                          TravelPlanMapper travelPlanMapper) {
        this.travelPlanService = travelPlanService;
        this.travelPlanMapper = travelPlanMapper;
    }

    @GetMapping("/plans")
    public Result<List<TravelPlan>> plans(@RequestParam Long requestId) {
        List<TravelPlan> travelPlans = travelPlanService.generatePlans(requestId);
        return Result.success(travelPlans);
    }
}