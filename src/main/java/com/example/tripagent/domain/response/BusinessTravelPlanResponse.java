package com.example.tripagent.domain.response;

import com.example.tripagent.domain.dto.ParsedTravelRequirement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessTravelPlanResponse {
    private String requestNo;
    private ParsedTravelRequirement requirement;
    private List<TravelPlanVO> plans;
    private TravelPlanVO recommendedPlan;
    private String approvalText;
    private Long auditId;
}
