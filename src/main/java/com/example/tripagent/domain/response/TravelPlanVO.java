package com.example.tripagent.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelPlanVO {

    private Long planId;

    private String planName;

    private TransportOptionVO transportOption;

    private HotelOptionVO hotelOption;

    private BigDecimal transportAmount;

    private BigDecimal hotelAmount;

    private BigDecimal mealAmount;

    private BigDecimal localTransportAmount;

    private BigDecimal totalAmount;

    /**
     * 合规状态：PASS / WARNING / REJECT
     */
    private String complianceStatus;

    /**
     * 方案评分，0-100
     */
    private Integer score;

    private String riskSummary;

    private String recommendReason;

    private List<ComplianceResultVO> complianceResults;
}