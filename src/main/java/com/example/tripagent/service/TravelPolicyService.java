package com.example.tripagent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.tripagent.domain.entity.TravelPolicy;

public interface TravelPolicyService extends IService<TravelPolicy> {
    TravelPolicy getByTravelLevel(String travelLevel);
}
