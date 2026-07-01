package com.example.tripagent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.tripagent.domain.entity.AgentAuditLog;

public interface AuditLogService extends IService<AgentAuditLog> {

    void recordSuccess(Long requestId,
                       String agentName,
                       String stepName,
                       String inputText,
                       String outputText);

    void recordFailure(Long requestId,
                       String agentName,
                       String stepName,
                       String inputText,
                       Exception e);
}