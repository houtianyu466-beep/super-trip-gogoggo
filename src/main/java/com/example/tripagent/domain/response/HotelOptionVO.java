package com.example.tripagent.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelOptionVO {

    private Long id;

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