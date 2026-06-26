package com.example.tripagent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tripagent.common.enums.ResponseCodeEnum;
import com.example.tripagent.common.exception.BusinessException;
import com.example.tripagent.domain.entity.TravelPolicy;
import com.example.tripagent.mapper.TravelPolicyMapper;
import com.example.tripagent.service.TravelPolicyService;
import org.springframework.stereotype.Service;

@Service
public class TravelPolicyServiceImpl
        extends ServiceImpl<TravelPolicyMapper, TravelPolicy>
        implements TravelPolicyService {
    @Override
    public TravelPolicy getByTravelLevel(String travelLevel) {
        if(travelLevel.isEmpty()){
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }
        TravelPolicy travelPolicy = getOne(new QueryWrapper<TravelPolicy>().eq("travel_level", travelLevel));

        if(travelPolicy == null){
            throw new BusinessException("找不到该等级的差旅政策");
        }
        return travelPolicy;
    }
}
