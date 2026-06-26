package com.example.tripagent.service;

import com.example.tripagent.domain.dto.ComplianceCheckResultDTO;
import com.example.tripagent.domain.entity.*;

public interface ComplianceCheckService {
    ComplianceCheckResultDTO check(TravelPlan plan,
                                   HotelOption hotel,
                                   TransportOption transport,
                                   BusinessTravelRequest request,
                                   TravelPolicy policy);
}
