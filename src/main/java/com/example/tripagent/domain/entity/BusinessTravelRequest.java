package com.example.tripagent.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("business_travel_request")
public class BusinessTravelRequest {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String requestNo;
    private Long employeeId;
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
    private String rawMessage;
    private String parsedJson;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
