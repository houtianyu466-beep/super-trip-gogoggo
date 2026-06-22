package com.example.tripagent.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParsedTravelRequirement {
    private String departureCity;
    private String destinationCity;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer travelDays;
    private String reason;
    private String projectName;
    private BigDecimal budgetAmount;
    private Boolean needTransport;
    private Boolean needHotel;
}