package com.example.tripagent.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelOptionDTO {
    private Long id;
    private Long requestId;
    private String hotelName;
    private String city;
    private String businessArea;
    private BigDecimal pricePerNight;
    private Integer nights;
    private BigDecimal totalPrice;
    private String distanceDesc;
    private Boolean agreementHotel;
    private BigDecimal rating;
    private Boolean recommended;
}

