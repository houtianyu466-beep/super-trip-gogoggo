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
@TableName("employee")
public class Employee {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String employeeNo;

    private String name;

    private String department;

    private String jobTitle;

    private String travelLevel;

    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
