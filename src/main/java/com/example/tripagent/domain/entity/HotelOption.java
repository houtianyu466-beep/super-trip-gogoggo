package com.example.tripagent.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("hotel_option")
public class HotelOption {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long requestId;
    private String hotelName;
    private String city;
    private String businessArea;
    private BigDecimal pricePerNight;
    private Integer nights;
    private BigDecimal totalPrice;
    private String distanceDesc;
    @TableField("is_agreement_hotel")
    private Boolean agreementHotel;
    private BigDecimal rating;
    @TableField("is_recommended")
    private Boolean recommended;
    private LocalDateTime createTime;
}
