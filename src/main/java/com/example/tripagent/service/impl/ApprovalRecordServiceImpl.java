package com.example.tripagent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tripagent.common.auth.UserContext;
import com.example.tripagent.common.enums.ResponseCodeEnum;
import com.example.tripagent.common.exception.BusinessException;
import com.example.tripagent.domain.constant.ApprovalStatusConstant;
import com.example.tripagent.domain.constant.TravelRequestStatusConstant;
import com.example.tripagent.domain.entity.ApprovalRecord;
import com.example.tripagent.domain.entity.BusinessTravelRequest;
import com.example.tripagent.mapper.ApprovalRecordMapper;
import com.example.tripagent.mapper.BusinessTravelRequestMapper;
import com.example.tripagent.service.ApprovalRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApprovalRecordServiceImpl
        extends ServiceImpl<ApprovalRecordMapper, ApprovalRecord>
        implements ApprovalRecordService {

    private final BusinessTravelRequestMapper businessTravelRequestMapper;

    public ApprovalRecordServiceImpl(BusinessTravelRequestMapper businessTravelRequestMapper) {
        this.businessTravelRequestMapper = businessTravelRequestMapper;
    }

    @Transactional
    @Override
    public ApprovalRecord submit(Long requestId) {
        if (requestId == null) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }

        BusinessTravelRequest request = businessTravelRequestMapper.selectById(requestId);
        if (request == null) {
            throw new BusinessException(ResponseCodeEnum.RESOURCE_NOT_FOUND);
        }
        Long currentEmployeeId = UserContext.getEmployeeId();
        if (currentEmployeeId == null) {
            throw new BusinessException(ResponseCodeEnum.PERMISSION_DENIED);
        }

        if (!currentEmployeeId.equals(request.getEmployeeId())) {
            throw new BusinessException(ResponseCodeEnum.PERMISSION_DENIED);
        }
        if (!TravelRequestStatusConstant.GENERATED.equals(request.getStatus())) {
            throw new BusinessException("当前出差申请状态不允许提交审批");
        }

        ApprovalRecord pendingRecord = getOne(
                new QueryWrapper<ApprovalRecord>()
                        .eq("request_id", requestId)
                        .eq("approval_status", ApprovalStatusConstant.PENDING)
                        .last("limit 1")
        );

        if (pendingRecord != null) {
            throw new BusinessException("当前出差申请已提交审批，请勿重复提交");
        }


        ApprovalRecord record = new ApprovalRecord();
        record.setRequestId(requestId);
        record.setApproverId(null);
        record.setApprovalLevel(1);
        record.setApprovalStatus(ApprovalStatusConstant.PENDING);
        record.setApprovalComment(null);
        record.setApprovalTime(null);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());

        save(record);

        request.setStatus(TravelRequestStatusConstant.SUBMITTED);
        request.setUpdateTime(LocalDateTime.now());
        businessTravelRequestMapper.updateById(request);

        return record;
    }

    @Override
    public ApprovalRecord approve(Long approvalRecordId, Long approverId, String comment) {
        return handleApproval(
                approvalRecordId,
                approverId,
                comment,
                ApprovalStatusConstant.APPROVED,
                TravelRequestStatusConstant.APPROVED
        );
    }

    @Override
    public ApprovalRecord reject(Long approvalRecordId, Long approverId, String comment) {
        return handleApproval(
                approvalRecordId,
                approverId,
                comment,
                ApprovalStatusConstant.REJECTED,
                TravelRequestStatusConstant.REJECTED
        );
    }

    @Override
    public ApprovalRecord getLatestByRequestId(Long requestId) {
        if (requestId == null) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }

        return getOne(
                new QueryWrapper<ApprovalRecord>()
                        .eq("request_id", requestId)
                        .orderByDesc("create_time")
                        .last("limit 1")
        );
    }

    @Override
    public List<ApprovalRecord> listByRequestId(Long requestId) {
        if (requestId == null) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }

        return list(
                new QueryWrapper<ApprovalRecord>()
                        .eq("request_id", requestId)
                        .orderByDesc("create_time")
        );
    }

    private ApprovalRecord handleApproval(Long approvalRecordId,
                                          Long approverId,
                                          String comment,
                                          String approvalStatus,
                                          String requestStatus) {
        if (approvalRecordId == null || approverId == null) {
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }

        ApprovalRecord record = getById(approvalRecordId);
        if (record == null) {
            throw new BusinessException(ResponseCodeEnum.RESOURCE_NOT_FOUND);
        }

        if (!"PENDING".equals(record.getApprovalStatus())) {
            throw new BusinessException(ApprovalStatusConstant.NOTPANDING);
        }

        record.setApproverId(approverId);
        record.setApprovalStatus(approvalStatus);

        record.setApprovalComment(comment);
        record.setApprovalTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());

        updateById(record);
        BusinessTravelRequest request = businessTravelRequestMapper.selectById(record.getRequestId());
        if (request == null) {
            throw new BusinessException(ResponseCodeEnum.RESOURCE_NOT_FOUND);
        }

        if (!TravelRequestStatusConstant.SUBMITTED.equals(request.getStatus())) {
            throw new BusinessException("当前出差申请不是审批中状态，不能审批");
        }
        if (request != null) {
            request.setStatus(requestStatus);
            request.setUpdateTime(LocalDateTime.now());
            businessTravelRequestMapper.updateById(request);
        }

        return record;
    }
}