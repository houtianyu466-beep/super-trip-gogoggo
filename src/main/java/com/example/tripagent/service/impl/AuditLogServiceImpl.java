package com.example.tripagent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tripagent.domain.entity.AgentAuditLog;
import com.example.tripagent.mapper.AgentAuditLogMapper;
import com.example.tripagent.service.AuditLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditLogServiceImpl
        extends ServiceImpl<AgentAuditLogMapper, AgentAuditLog>
        implements AuditLogService {

    @Override
    public void recordSuccess(Long requestId,
                              String agentName,
                              String stepName,
                              String inputText,
                              String outputText) {
        AgentAuditLog log = new AgentAuditLog();
        log.setRequestId(requestId);
        log.setAgentName(agentName);
        log.setStepName(stepName);
        log.setInputText(inputText);
        log.setOutputText(outputText);
        log.setSuccess(true);
        log.setErrorMessage(null);
        log.setCreateTime(LocalDateTime.now());

        save(log);
    }

    @Override
    public void recordFailure(Long requestId,
                              String agentName,
                              String stepName,
                              String inputText,
                              Exception e) {
        AgentAuditLog log = new AgentAuditLog();
        log.setRequestId(requestId);
        log.setAgentName(agentName);
        log.setStepName(stepName);
        log.setInputText(inputText);
        log.setOutputText(null);
        log.setSuccess(false);
        log.setErrorMessage(e.getMessage());
        log.setCreateTime(LocalDateTime.now());

        save(log);
    }
}