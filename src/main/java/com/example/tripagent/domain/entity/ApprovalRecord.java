package com.example.tripagent.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("approval_record")
public class ApprovalRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long requestId;
    private Long approverId;
    private Integer approvalLevel;
    private String approvalStatus;
    private String approvalComment;
    private LocalDateTime approvalTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
