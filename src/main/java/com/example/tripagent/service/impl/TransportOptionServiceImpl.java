package com.example.tripagent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tripagent.common.enums.ResponseCodeEnum;
import com.example.tripagent.common.exception.BusinessException;
import com.example.tripagent.domain.entity.BusinessTravelRequest;
import com.example.tripagent.domain.entity.Employee;
import com.example.tripagent.domain.entity.TransportOption;
import com.example.tripagent.domain.entity.TravelPolicy;
import com.example.tripagent.mapper.BusinessTravelRequestMapper;
import com.example.tripagent.mapper.EmployeeMapper;
import com.example.tripagent.mapper.TransportOptionMapper;
import com.example.tripagent.mapper.TravelPolicyMapper;
import com.example.tripagent.service.TransportOptionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransportOptionServiceImpl
        extends ServiceImpl<TransportOptionMapper, TransportOption>
        implements TransportOptionService {

    private final TransportOptionMapper transportOptionMapper;
    private final BusinessTravelRequestMapper businessTravelRequestMapper;
    private final EmployeeMapper employeeMapper;
    private final TravelPolicyMapper travelPolicyMapper;
    public TransportOptionServiceImpl(TransportOptionMapper transportOptionMapper,
                                      BusinessTravelRequestMapper businessTravelRequestMapper,
                                      EmployeeMapper employeeMapper,
                                      TravelPolicyMapper travelPolicyMapper) {
        this.transportOptionMapper = transportOptionMapper;
        this.businessTravelRequestMapper = businessTravelRequestMapper;
        this.employeeMapper = employeeMapper;
        this.travelPolicyMapper = travelPolicyMapper;
    }


    @Override
    public List<TransportOption> getVaildOptions(Long requestId) {
        if (requestId == null) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }

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

        String transportLevel = policy.getTransportLevel();

        QueryWrapper<TransportOption> queryWrapper = new QueryWrapper<TransportOption>()
                .eq("request_id", requestId)
                .eq("departure_city", request.getDepartureCity())
                .eq("arrival_city", request.getDestinationCity());

        queryWrapper.and(wrapper -> {
            if (transportLevel.contains("FLIGHT_ECONOMY")) {
                wrapper.or(w -> w.eq("transport_type", "FLIGHT").eq("seat_level", "ECONOMY"));
            }
            if (transportLevel.contains("FLIGHT_BUSINESS")) {
                wrapper.or(w -> w.eq("transport_type", "FLIGHT").eq("seat_level", "BUSINESS"));
            }
            if (transportLevel.contains("TRAIN_SECOND_CLASS")) {
                wrapper.or(w -> w.eq("transport_type", "TRAIN").eq("seat_level", "SECOND_CLASS"));
            }
            if (transportLevel.contains("TRAIN_FIRST_CLASS")) {
                wrapper.or(w -> w.eq("transport_type", "TRAIN").eq("seat_level", "FIRST_CLASS"));
            }
        });

        queryWrapper.orderByAsc("price");

        return transportOptionMapper.selectList(queryWrapper);
    }
}
