package com.example.tripagent.controller;

import com.example.tripagent.application.BusinessTravelWorkflowService;
import com.example.tripagent.common.auth.UserContext;
import com.example.tripagent.common.result.Result;
import com.example.tripagent.domain.entity.ApprovalRecord;
import com.example.tripagent.domain.request.BusinessTravelPlanRequest;
import com.example.tripagent.domain.response.BusinessTravelPlanResponse;
import com.example.tripagent.domain.vo.ApprovalRecordVO;
import com.example.tripagent.service.ApprovalRecordService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/businessTravel")
public class BusinessTravelController {

    private final BusinessTravelWorkflowService businessTravelWorkflowService;
    private final ApprovalRecordService approvalRecordService;
    public BusinessTravelController(BusinessTravelWorkflowService businessTravelWorkflowService,
                                    ApprovalRecordService approvalRecordService) {
        this.businessTravelWorkflowService = businessTravelWorkflowService;
        this.approvalRecordService = approvalRecordService;
    }

    @PostMapping("/plan")
    public Result<BusinessTravelPlanResponse> plan(@Valid @RequestBody BusinessTravelPlanRequest request) {
        request.setEmployeeId(UserContext.getEmployeeId());
        BusinessTravelPlanResponse businessTravelPlanResponse = businessTravelWorkflowService.generatePlan(request);
        return Result.success(businessTravelPlanResponse);
    }

    @GetMapping("/detail")
    public Result<BusinessTravelPlanResponse> detail(@RequestParam String requestNo) {
        BusinessTravelPlanResponse response =
                businessTravelWorkflowService.getPlanDetailByRequestNo(requestNo);
        return Result.success(response);
    }
    @PostMapping("/approval/submit")
    public Result<ApprovalRecordVO> submitApproval(@RequestParam Long requestId) {
        ApprovalRecord record = approvalRecordService.submit(requestId);
        return Result.success(convertToApprovalRecordVO(record));
    }

    @PostMapping("/approval/approve")
    public Result<ApprovalRecordVO> approve(@RequestParam Long approvalRecordId,
                                            @RequestParam(required = false) String comment) {
        Long approverId = UserContext.getEmployeeId();

        ApprovalRecord record = approvalRecordService.approve(
                approvalRecordId,
                approverId,
                comment
        );

        return Result.success(convertToApprovalRecordVO(record));
    }

    @PostMapping("/approval/reject")
    public Result<ApprovalRecordVO> reject(@RequestParam Long approvalRecordId,
                                           @RequestParam(required = false) String comment) {
        Long approverId = UserContext.getEmployeeId();

        ApprovalRecord record = approvalRecordService.reject(
                approvalRecordId,
                approverId,
                comment
        );

        return Result.success(convertToApprovalRecordVO(record));
    }
    @GetMapping("/approval/latest")
    public Result<ApprovalRecordVO> latestApproval(@RequestParam Long requestId) {
        ApprovalRecord record = approvalRecordService.getLatestByRequestId(requestId);
        return Result.success(convertToApprovalRecordVO(record));
    }

    @GetMapping("/approval/list")
    public Result<List<ApprovalRecordVO>> listApproval(@RequestParam Long requestId) {
        List<ApprovalRecordVO> records = approvalRecordService.listByRequestId(requestId)
                .stream()
                .map(this::convertToApprovalRecordVO)
                .collect(Collectors.toList());

        return Result.success(records);
    }

    private ApprovalRecordVO convertToApprovalRecordVO(ApprovalRecord record) {
        if (record == null) {
            return null;
        }

        ApprovalRecordVO vo = new ApprovalRecordVO();
        vo.setId(record.getId());
        vo.setRequestId(record.getRequestId());
        vo.setApproverId(record.getApproverId());
        vo.setApprovalLevel(record.getApprovalLevel());
        vo.setApprovalStatus(record.getApprovalStatus());
        vo.setApprovalComment(record.getApprovalComment());
        vo.setApprovalTime(record.getApprovalTime());
        vo.setCreateTime(record.getCreateTime());

        return vo;
    }
}
