package com.example.tripagent.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tripagent.common.enums.ResponseCodeEnum;
import com.example.tripagent.common.exception.BusinessException;
import com.example.tripagent.domain.entity.BusinessTravelRequest;
import com.example.tripagent.domain.entity.Employee;
import com.example.tripagent.domain.entity.HotelOption;
import com.example.tripagent.domain.entity.TravelPolicy;
import com.example.tripagent.mapper.BusinessTravelRequestMapper;
import com.example.tripagent.mapper.EmployeeMapper;
import com.example.tripagent.mapper.HotelOptionMapper;
import com.example.tripagent.mapper.TravelPolicyMapper;
import com.example.tripagent.service.HotelOptionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class HotelOptionServiceImpl
        extends ServiceImpl<HotelOptionMapper, HotelOption>
        implements HotelOptionService {
    private final HotelOptionMapper hotelOptionMapper;
    private final EmployeeMapper employeeMapper;
    private final BusinessTravelRequestMapper businessTravelRequestMapper;
    private final TravelPolicyMapper travelPolicyMapper;
    public HotelOptionServiceImpl(HotelOptionMapper hotelOptionMapper,
                                  BusinessTravelRequestMapper businessTravelRequestMapper,
                                  EmployeeMapper employeeMapper,
                                  TravelPolicyMapper travelPolicyMapper) {
        this.hotelOptionMapper = hotelOptionMapper;
        this.businessTravelRequestMapper = businessTravelRequestMapper;
        this.employeeMapper = employeeMapper;
        this.travelPolicyMapper = travelPolicyMapper;
    }
    @Override
    public List<HotelOption> getVaildOptions(Long requestId) {
        BusinessTravelRequest request = businessTravelRequestMapper.selectById(requestId);
        if (request == null) {
            throw new BusinessException(ResponseCodeEnum.REQUEST_NOT_EXIST);
        }

        Employee employee = employeeMapper.selectById(request.getEmployeeId());
        if (employee == null) {
            throw new BusinessException(ResponseCodeEnum.EMPLOYEE_NOT_EXIST);
        }

        String travelLevel = employee.getTravelLevel();
        if (travelLevel == null || travelLevel.trim().isEmpty()) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }

        TravelPolicy policy = travelPolicyMapper.selectOne(
                new QueryWrapper<TravelPolicy>().eq("travel_level", travelLevel)
        );
        if (policy == null) {
            throw new BusinessException("找不到对应差旅政策");
        }

        BigDecimal hotelLimitPerNight = policy.getHotelLimitPerNight();
        String destinationCity = request.getDestinationCity();
        Integer travelDays = request.getTravelDays();

        List<HotelOption> options = hotelOptionMapper.selectList(
                new QueryWrapper<HotelOption>()
                        .eq("request_id", requestId)
                        .eq("city", destinationCity)
                        .le("price_per_night", hotelLimitPerNight)
                        .orderByDesc("is_agreement_hotel")
                        .orderByDesc("rating")
                        .orderByAsc("price_per_night")
        );

        if (travelDays != null) {
            options.forEach(o -> o.setNights(travelDays));
        }

        return options;

    }
}
