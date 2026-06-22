package com.example.tripagent.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("compliance_result")
public class ComplianceResult {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long requestId;

    private Long planId;

    /**
     * 检查项：HOTEL_LIMIT / TRANSPORT_LEVEL / BUDGET_LIMIT / DAYS_REASONABLE
     */
    private String checkItem;

    /**
     * 检查结果：PASS / WARNING / FAIL
     */
    private String checkStatus;

    /**
     * 实际值，例如：600元/晚、BUSINESS、3200元
     */
    private String actualValue;

    /**
     * 限制值，例如：500元/晚、ECONOMY、3000元
     */
    private String limitValue;

    /**
     * 校验说明
     */
    private String message;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}