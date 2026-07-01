package com.example.tripagent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tripagent.domain.entity.BusinessTravelRequest;
import com.example.tripagent.mapper.BusinessTravelRequestMapper;
import com.example.tripagent.service.BusinessTravelRequestService;
import org.springframework.stereotype.Service;

@Service
public class BusinessTravelRequestServiceImpl
        extends ServiceImpl<BusinessTravelRequestMapper, BusinessTravelRequest>
        implements BusinessTravelRequestService {
}
