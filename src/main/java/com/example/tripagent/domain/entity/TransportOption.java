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
@AllArgsConstructor
@NoArgsConstructor
@TableName("transport_option")
public class TransportOption {
    @TableId(type = IdType.AUTO)
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
    @TableField("is_recommended")
    private Boolean recommended;
    private LocalDateTime createTime;
}
