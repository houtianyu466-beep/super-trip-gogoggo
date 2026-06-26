package com.example.tripagent.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransportOptionDTO {
    private Long id;
    private Long requestId;
    private String transportType;
    private String seatLevel;
    private String departureCity;
    private String arrivalCity;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private BigDecimal price;
    private Integer durationMinutes;
    private String provider;
    private Boolean recommended;
}

