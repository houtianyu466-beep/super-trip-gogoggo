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
@TableName("travel_plan")
public class TravelPlan {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long requestId;
    private String planName;
    private Long transportOptionId;
    private Long hotelOptionId;
    private BigDecimal transportAmount;
    private BigDecimal hotelAmount;
    private BigDecimal mealAmount;
    private BigDecimal localTransportAmount;
    private BigDecimal totalAmount;
    private String complianceStatus;
    private Integer score;
    private String riskSummary;
    private String recommendReason;
    private String approvalText;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
