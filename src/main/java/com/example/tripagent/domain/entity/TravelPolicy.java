package com.example.tripagent.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("travel_policy")
public class TravelPolicy {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String travelLevel;
    private BigDecimal hotelLimitPerNight;
    private String transportLevel;
    private BigDecimal mealAllowancePerDay;
    private BigDecimal localTransportPerDay;
    private BigDecimal approvalRequiredAmount;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
