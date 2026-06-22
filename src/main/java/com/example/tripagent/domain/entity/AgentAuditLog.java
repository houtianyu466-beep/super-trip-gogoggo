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
@TableName("agent_audit_log")
public class AgentAuditLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long requestId;

    private String agentName;

    private String stepName;

    private String inputText;

    private String outputText;

    private String toolName;

    private String toolInput;

    private String toolOutput;

    private Boolean success;

    private String errorMessage;

    private LocalDateTime createTime;
}