package com.example.tripagent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.tripagent.domain.entity.BusinessTravelRequest;
import com.example.tripagent.domain.entity.TravelPlan;

import java.util.List;

public interface TravelPlanService extends IService<TravelPlan> {
    List<TravelPlan> generatePlans(Long requestId);
}
