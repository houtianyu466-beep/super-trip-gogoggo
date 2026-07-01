package com.example.tripagent.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRecordVO {

    private Long id;

    private Long requestId;

    private Long approverId;

    private Integer approvalLevel;

    private String approvalStatus;

    private String approvalComment;

    private LocalDateTime approvalTime;

    private LocalDateTime createTime;
}