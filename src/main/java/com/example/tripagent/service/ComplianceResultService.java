package com.example.tripagent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.tripagent.domain.entity.BusinessTravelRequest;
import com.example.tripagent.domain.entity.ComplianceResult;
import com.example.tripagent.domain.entity.TravelPlan;

import java.util.List;

public interface ComplianceResultService extends IService<ComplianceResult> {

    void removeByRequestId(Long requestId);

    void savePlanResults(Long requestId, BusinessTravelRequest request, List<TravelPlan> plans);
}