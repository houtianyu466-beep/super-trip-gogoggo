package com.example.tripagent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.tripagent.domain.entity.ApprovalRecord;

import java.util.List;

public interface ApprovalRecordService extends IService<ApprovalRecord> {

     ApprovalRecord submit(Long requestId);

    ApprovalRecord approve(Long approvalRecordId, Long approverEmployeeId, String comment);

    ApprovalRecord reject(Long approvalRecordId, Long approverEmployeeId, String comment);
    ApprovalRecord getLatestByRequestId(Long requestId);

    List<ApprovalRecord> listByRequestId(Long requestId);
}